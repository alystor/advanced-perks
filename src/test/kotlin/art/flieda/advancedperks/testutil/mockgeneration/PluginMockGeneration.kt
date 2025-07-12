package art.flieda.advancedperks.testutil.mockgeneration

import io.mockk.every
import io.mockk.mockk
import org.bukkit.plugin.Plugin
import java.util.logging.Logger

object PluginMockGeneration {

    fun aPluginMock(): Plugin = mockk<Plugin>().apply {
        every { logger } returns Logger.getLogger("mock-plugin-logger")
    }

}
