package art.flieda.advancedperks.configuration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.collections.iterator

open class Configuration(
    val classPathUrl: String,
) : KoinComponent {

    val pluginDirectory: File = get<File>(named("pluginDirectory"))
    val yaml: Yaml = get<Yaml>()
    val configurationFile: File = loadConfigurationFile()
    val data: MutableMap<String, Any?> = configurationFile.reader().use { reader ->
        yaml.load<MutableMap<String, Any?>>(reader).flattenMapKeys()
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val dataMutex = Mutex()

    fun getStringList(key: String, default: List<String>? = null): List<String>? = getGeneric(key, default)

    fun getString(key: String, default: String? = null): String? = getGeneric(key, default)

    fun getNumber(key: String, default: Number): Number = getGeneric(key, default)

    fun getNumber(key: String): Number? = getGeneric(key, null)

    fun getBoolean(key: String, default: Boolean? = null): Boolean? = getGeneric(key, default)

    fun setValue(key: String, value: Any) = runBlocking {
        dataMutex.withLock {
            data[key] = value
        }
    }

    fun saveConfiguration() = coroutineScope.launch {
        withContext(Dispatchers.IO) {
            val dataToSave = dataMutex.withLock {
                data.restoreNestedMap()
            }
            configurationFile.writer().use { writer ->
                yaml.dump(dataToSave, writer)
            }
        }
    }

    fun <T> T?.requireValue(): T = checkNotNull(this) { "Expected configuration value that was null." }

    private inline fun <reified T> getGeneric(key: String, defaultValue: T): T {
        val value = data[key]

        require(value is T || value == null) {
            "Value for key '$key' cannot be cast to the expected type '${T::class.java.name}'."
        }

        return value ?: defaultValue
    }

    private fun MutableMap<String, Any?>.restoreNestedMap(): MutableMap<String, Any?> {
        val nestedMap = mutableMapOf<String, Any?>()

        for ((key, value) in this) {
            val pathSegments = key.split(".")
            var currentMap = nestedMap

            for ((index, segment) in pathSegments.withIndex()) {
                if (index == pathSegments.size - 1) {
                    currentMap[segment] = value
                } else {
                    currentMap =
                        currentMap.computeIfAbsent(segment) { mutableMapOf<String, Any?>() } as MutableMap<String, Any?>
                }
            }
        }

        return nestedMap
    }

    private fun MutableMap<String, Any?>.flattenMapKeys(): MutableMap<String, Any?> {
        val flattenedMap = mutableMapOf<String, Any?>()

        fun flattenRecursive(currentMap: Map<String, Any?>, pathSegments: List<String>) {
            for ((key, value) in currentMap) {
                val nextPathSegments = pathSegments + key

                if (value is Map<*, *>) {
                    flattenRecursive(value as Map<String, Any?>, nextPathSegments)
                } else {
                    val fullPath = nextPathSegments.joinToString(".")
                    flattenedMap[fullPath] = value
                }
            }
        }
        flattenRecursive(this, emptyList())

        return flattenedMap
    }

    private fun loadConfigurationFile(): File {
        val configurationFile = File(pluginDirectory, classPathUrl)
        if (!configurationFile.exists()) {
            extractConfigurationFileFromJar(configurationFile)
        }
        return configurationFile
    }

    private fun extractConfigurationFileFromJar(configurationFile: File) {
        configurationFile.parentFile.mkdirs()
        val inputStream = this::class.java.classLoader.getResourceAsStream(classPathUrl)

        check(inputStream != null) {
            "Configuration file for configuration class '${this::javaClass.name}' not found in the classpath under the path: '$classPathUrl'"
        }

        inputStream.use { inputStream ->
            Files.copy(inputStream, configurationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
    }

}
