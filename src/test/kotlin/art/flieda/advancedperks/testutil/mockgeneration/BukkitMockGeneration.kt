package art.flieda.advancedperks.testutil.mockgeneration

import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginManager
import java.util.UUID

object BukkitMockGeneration {

    fun runWithBukkitMock(
        getPlayerMocks: List<Pair<UUID, Player>> = emptyList(),
        block: (PluginManager) -> Unit,
    ) {
        mockkStatic(Bukkit::class)
        val pluginManager = mockk<PluginManager>()
        every { pluginManager.registerEvents(any(), any()) } just Runs

        every { Bukkit.getPlayer(any<UUID>()) } returns null
        getPlayerMocks.forEach { pair ->
            every { Bukkit.getPlayer(pair.first) } returns pair.second
        }

        every { Bukkit.getPluginManager() } returns pluginManager

        block(pluginManager)

        unmockkStatic(Bukkit::class)
    }

}
