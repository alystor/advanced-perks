package art.flieda.advancedperks.perkdata

import art.flieda.advancedperks.AdvancedPerks.Companion.logger
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

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            repository.findPerkDataById(event.player.uniqueId)?.let { perkDataProjection ->
                Bukkit.getPlayer(event.player.uniqueId)?.let { player ->
                    player.getOrCreatePerkData().mergeWithLoadedProjection(perkDataProjection)
                    logger.fine { "PerkData for player '${player.name}' with UUID '${player.uniqueId}' has been loaded and set successfully." }
                }
            }?: event.player.getOrCreatePerkData().apply { loadedFromDatasource = true }
        })
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            val perkData = event.player.getOrCreatePerkData()
            if (perkData.loadedFromDatasource) {
                repository.savePerkData(perkData)
                logger.fine { "PerkData for player '${event.player.name}' with UUID '${event.player.uniqueId}' has been saved successfully." }
            }
        })
    }

}
