package art.flieda.advancedperks.configuration

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
import kotlin.reflect.KClass

const val CONFIGURATION_PATH_SEPARATOR = "."

open class Configuration(
    val classPathUrl: String,
) : KoinComponent {

    val objectMapper = get<ObjectMapper>()
    val pluginDirectory: File = get<File>(named("pluginDirectory"))
    val yaml: Yaml = get<Yaml>()
    val configurationFile: File = loadConfigurationFile()
    val data: MutableMap<String, Any?> = configurationFile.reader().use { reader ->
        yaml.load<MutableMap<String, Any?>>(reader)
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val dataMutex = Mutex()

    fun setValue(key: String, value: Any?) {
        val pathParts = key.split(CONFIGURATION_PATH_SEPARATOR).filter { it.isNotEmpty() }
        var current: Any? = data

        pathParts.forEachIndexed { index, subPath ->
            val isLastIndex = index == pathParts.lastIndex
            if (isLastIndex) {
                if (value == null) {
                    (current as MutableMap<String, Any?>).remove(subPath)
                } else {
                    (current as MutableMap<String, Any?>).put(subPath, value)
                }
            } else {
                current = (current as Map<*, *>)[subPath]
            }
        }
    }

    inline fun <reified T> getListOfAny(key: String = ""): List<T>? =
        getAny<List<Any>>(key)?.map { objectMapper.convertValue<T>(it, T::class.java) }

    inline fun <reified T : Any> getAny(key: String = "", returnType: KClass<T> = T::class): T? {
        val pathParts = key.split(CONFIGURATION_PATH_SEPARATOR).filter { it.isNotEmpty() }
        var current: Any? = data

        pathParts.forEachIndexed { index, subPath ->
            val isLastIndex = index == pathParts.lastIndex
            current = if (isLastIndex) {
                (current as Map<*, *>)[subPath]
            } else {
                val value = (current as Map<*, *>)[subPath]
                value as? Map<*, *>
            }
        }

        return if (T::class.java.isEnum) {
            T::class.java.enumConstants?.find { (it as Enum<*>).name == current?.toString()?.uppercase() }
        } else {
            objectMapper.convertValue(current, returnType.java)
        }
    }

    fun saveConfiguration() = coroutineScope.launch {
        withContext(Dispatchers.IO) {
            val dataToSave = dataMutex.withLock { data }
            configurationFile.writer().use { writer ->
                yaml.dump(dataToSave, writer)
            }
        }
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

fun <T> T?.orDefault(defaultValue: T): T =
    this ?: defaultValue

fun <T> T?.requireValue(): T = requireNotNull(this) {
    "Required configuration value is null."
}
