package art.flieda.advancedperks.perkdata

import art.flieda.advancedperks.AdvancedPerks.Companion.logger
import art.flieda.advancedperks.core.runTaskAsync
import art.flieda.advancedperks.core.runTaskSync
import art.flieda.advancedperks.lifecycle.PluginOnDisableHandler
import art.flieda.advancedperks.lifecycle.PluginOnEnableHandler
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.repository.PerkDataRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent

//TODO write tests for this
class PerkDataController(
    private val repository: PerkDataRepository,
    private val registry: PerkRegistry,
    private val stateController: PerkStateController,
    plugin: Plugin,
) : KoinComponent, Listener, PluginOnDisableHandler, PluginOnEnableHandler {

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    override fun handlePluginOnEnable() {
        Bukkit.getOnlinePlayers().forEach { player -> player.loadAndEnablePerks() }
    }

    override fun handlePluginOnDisable() {
        runBlocking {
            val saveJobs = Bukkit.getOnlinePlayers().map { player -> player.saveAndDisablePerks() }
            //TODO maybe add a timeout
            saveJobs.joinAll()
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerJoin(event: PlayerJoinEvent) {
        event.player.loadAndEnablePerks()
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlayerQuit(event: PlayerQuitEvent) {
        event.player.saveAndDisablePerks()
    }

    private fun Player.saveAndDisablePerks(): Job {
        val perkData = getOrCreatePerkData()
        val perkDataCopy = perkData.deepCopy()

        perkData.getEnabledAndLoadedPerks().forEach { perk ->
            stateController.forceDisablePerk(this, perk)
        }

        return runTaskAsync {
            if (perkDataCopy.loadedFromDatasource) {
                repository.savePerkData(perkDataCopy)
                logger.fine { "PerkData for player '${name}' with UUID '${uniqueId}' has been saved successfully." }
            }
        }
    }

    private fun Player.loadAndEnablePerks() {
        runTaskAsync {
            val perkDataProjection = repository.findPerkDataById(uniqueId)
            val perkData = getOrCreatePerkData()

            runTaskSync {
                if (perkDataProjection != null) {
                    perkDataProjection.enabledPerks.forEachExistingPerk { perk ->
                        stateController.forceEnablePerk(this, perk)
                    }
                    perkData.boughtPerks.addAll(perkDataProjection.boughtPerks)

                    logger.fine { "PerkData for player '${name}' with UUID '${uniqueId}' has been loaded and set successfully." }
                }
                perkData.loadedFromDatasource = true
            }
        }
    }

    private fun Set<String>.forEachExistingPerk(action: (Perk) -> Unit) =
        mapNotNull { perkIdentifier ->
            registry.findPerkByIdentifier(perkIdentifier)
        }.forEach { perk ->
            action(perk)
        }
}
