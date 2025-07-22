package art.flieda.advancedperks.compatibility.bukkit

import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PlayerJoinAndQuitCompatibilityHandler : Listener, KoinComponent {

    val plugin = get<Plugin>()
    val perkStateController = get<PerkStateController>()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        player.getOrCreatePerkData().getEnabledAndLoadedPerks().forEach { perk ->
            perkStateController.forceDisablePerk(player, perk)
        }
    }

}
