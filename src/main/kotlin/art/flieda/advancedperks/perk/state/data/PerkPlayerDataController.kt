package art.flieda.advancedperks.perk.state.data

import art.flieda.advancedperks.AdvancedPerks.Companion.logger
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

//TODO write tests for class
class PerkPlayerDataController : KoinComponent, Listener {

    val repository: PerkPlayerDataRepository = get<PerkPlayerDataRepository>()
    val plugin: Plugin = get<Plugin>()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            event.player.getOrCreatePerkData().let { perkData ->
                if (!(perkData.loadedFromDatasource)) {
                    logger.fine { "PerkData for player '${event.player.name}' with UUID '${event.player.uniqueId}' was never loaded from datasource, skipping save." }
                } else {
                    repository.savePerkPlayerData(perkData)
                    logger.fine { "PerkData for player '${event.player.name}' with UUID '${event.player.uniqueId}' has been saved successfully." }
                }
            }
        })
    }

    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        val uuid = event.player.uniqueId
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            repository.loadPerkPlayerData(event.player).let { perkData ->
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    Bukkit.getPlayer(uuid).let { player ->
                        if (player == null) {
                            logger.fine { "Unable to set PerkData, player with UUID '$uuid' has disconnected after data was loaded." }
                        } else {
                            player.getOrCreatePerkData().mergeLoadedPerkData(perkData)
                            logger.fine { "PerkData for player '${player.name}' with UUID '$uuid' has been loaded and set successfully." }
                        }
                    }
                })
            }
        })
    }

}
