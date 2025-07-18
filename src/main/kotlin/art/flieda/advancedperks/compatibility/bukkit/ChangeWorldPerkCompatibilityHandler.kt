package art.flieda.advancedperks.compatibility.bukkit

import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class ChangeWorldPerkCompatibilityHandler : Listener, KoinComponent {

    val plugin = get<Plugin>()
    val perkRegistry = get<PerkRegistry>()
    val perkStateController = get<PerkStateController>()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onPlayerChangedWorld(event: PlayerChangedWorldEvent) {
        val player = event.player
        val newWorldName = player.world.name

        player.getOrCreatePerkData().getEnabledAndLoadedPerks()
            .forEach { perk ->
                if (perk.getDisallowedWorlds().any { it.equals(newWorldName, ignoreCase = true) }) {
                    perkStateController.forceDisablePerk(player, perk)
                }
            }
    }

}
