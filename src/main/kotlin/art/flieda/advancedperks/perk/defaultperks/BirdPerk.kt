package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.DisableActionPerk
import art.flieda.advancedperks.perk.types.EnableActionPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerGameModeChangeEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.Plugin
import org.koin.core.component.get

class BirdPerk : AbstractPerk("bird"), EnableActionPerk, DisableActionPerk, ListenerPerk {

    val plugin = get<Plugin>()

    override fun onPerkEnable(player: Player) {
        player.setFlyingState(true)
    }

    override fun onPerkDisable(player: Player) {
        if (player.gameMode in listOf(GameMode.SURVIVAL, GameMode.ADVENTURE)) {
            player.setFlyingState(false)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        event.player.runWhenPerkIsEnabled { player ->
            player.setFlyingState(true)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChangedWorld(event: PlayerChangedWorldEvent) {
        event.player.runWhenPerkIsEnabled { player ->
            player.setFlyingState(true)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerGameModeChange(event: PlayerGameModeChangeEvent) {
        event.player.runWhenPerkIsEnabled { player ->
            Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                player.setFlyingState(true)
            }, 1)
        }
    }

    private fun Player.setFlyingState(state: Boolean) = this.apply {
        allowFlight = state
        isFlying = state
    }

}
