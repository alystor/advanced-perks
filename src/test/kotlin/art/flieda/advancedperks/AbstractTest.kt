package art.flieda.advancedperks

import art.flieda.advancedperks.configuration.ConfigurationModule
import art.flieda.advancedperks.testutil.mockgeneration.PluginMockGeneration
import art.flieda.advancedperks.testutil.testdatageneration.TestDataGeneration
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.bukkit.plugin.Plugin
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestWatcher
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists

@ExtendWith(AbstractTestWatcher::class)
abstract class AbstractTest(
    vararg modules: Module
) {

    init {
        stopKoin()
        startKoin {
            modules(
                *modules, module {
                    single<Yaml> { Yaml() }
                    single<File>(named("pluginDirectory")) { PLUGIN_FOLDER_PATH.toFile() }
                },
                ConfigurationModule.objectMapperModule
            )
        }
    }

    fun addPluginMock(): Plugin =
        PluginMockGeneration.aPluginMock().also { plugin -> loadKoinModules(module { single<Plugin> { plugin } }) }

    @BeforeEach
    fun removeFiles(testInfo: TestInfo) {
        println(" ")
        println("---[${testInfo.testClass.get().simpleName}.${testInfo.displayName}]---")
        TestDataGeneration.resetSeed()
        Files.walk(PLUGIN_FOLDER_PATH)
            .sorted(Comparator.reverseOrder())
            .filter { it != PLUGIN_FOLDER_PATH }
            .forEach { Files.delete(it) }
    }

    companion object {
        lateinit var PLUGIN_FOLDER_PATH: Path
            private set

        @BeforeAll
        @JvmStatic
        fun setupTestSuite() {
            PLUGIN_FOLDER_PATH = Paths.get("build", "tmp", "test-plugin-data-folder")
            Files.createDirectories(PLUGIN_FOLDER_PATH)
        }

        @OptIn(ExperimentalPathApi::class)
        @AfterAll
        @JvmStatic
        fun cleanupTestSuite() {
            if (PLUGIN_FOLDER_PATH.exists()) {
                PLUGIN_FOLDER_PATH.deleteRecursively()
            }
        }
    }

}

class AbstractTestWatcher : TestWatcher {
    override fun testSuccessful(context: ExtensionContext?) = println("---[Test run was SUCCESSFUL]---")
    override fun testFailed(context: ExtensionContext?, cause: Throwable?) = println("---[Test run FAILED]---")
}
