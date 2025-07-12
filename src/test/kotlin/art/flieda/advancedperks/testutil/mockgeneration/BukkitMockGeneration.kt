package art.flieda.advancedperks.testutil.mockgeneration

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.bukkit.Bukkit
import org.bukkit.plugin.PluginManager

object BukkitMockGeneration {

    fun runWithBukkitMock(
        block: (PluginManager) -> Unit
    ) {
        mockkStatic(Bukkit::class)
        val pluginManager = mockk<PluginManager>()
        every { pluginManager.registerEvents(any(), any()) } just Runs

        every { Bukkit.getPluginManager() } returns pluginManager

        block(pluginManager)

        unmockkStatic(Bukkit::class)
    }

}
