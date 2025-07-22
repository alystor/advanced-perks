package art.flieda.advancedperks.perkdata

import art.flieda.advancedperks.AdvancedPerks.Companion.logger
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.repository.PerkDataRepository
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent

class PerkDataController(
    private val repository: PerkDataRepository,
    private val registry: PerkRegistry,
    private val stateController: PerkStateController,
    private val plugin: Plugin,
) : KoinComponent, Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerLogin(event: PlayerLoginEvent) {
        if (event.result != PlayerLoginEvent.Result.ALLOWED) {
            return
        }

        val player = event.player

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            repository.findPerkDataById(player.uniqueId).let { perkDataProjection ->
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    if (perkDataProjection != null) {
                        perkDataProjection.enabledPerks.mapNotNull { enabledPerk ->
                            registry.findPerkByIdentifier(enabledPerk)
                        }.forEach { perk ->
                            stateController.forceEnablePerk(player, perk)
                        }

                        player.getOrCreatePerkData().boughtPerks.addAll(perkDataProjection.boughtPerks)
                        player.getOrCreatePerkData().loadedFromDatasource = true

                        logger.fine { "PerkData for player '${player.name}' with UUID '${player.uniqueId}' has been loaded and set successfully." }
                    } else {
                        player.getOrCreatePerkData().apply { loadedFromDatasource = true }
                    }
                })
            }
        })
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val perkData = player.getOrCreatePerkData()
        val perkDataToSave = perkData.copy()

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            if (perkDataToSave.loadedFromDatasource) {
                repository.savePerkData(perkDataToSave)
                logger.fine { "PerkData for player '${event.player.name}' with UUID '${event.player.uniqueId}' has been saved successfully." }
            }
        })

        perkData.getEnabledAndLoadedPerks().forEach { perk ->
            stateController.forceDisablePerk(player, perk)
        }

        player.removePerkData()
    }

}
