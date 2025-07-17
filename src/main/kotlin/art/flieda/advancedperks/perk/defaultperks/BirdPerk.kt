package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.DisableActionPerk
import art.flieda.advancedperks.perk.types.EnableActionPerk
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerGameModeChangeEvent

class BirdPerk : AbstractPerk("bird"), EnableActionPerk, DisableActionPerk {

    override fun onPerkEnable(player: Player) {
        player.setFlyingState(true)
    }

    override fun onPerkDisable(player: Player) {
        if (player.gameMode in listOf(GameMode.SURVIVAL, GameMode.ADVENTURE)) {
            player.setFlyingState(false)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerChangedWorld(event: PlayerChangedWorldEvent) {
        runWhenPerkIsEnabled(event.player) { player ->
            player.setFlyingState(true)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun onPlayerGameModeChange(event: PlayerGameModeChangeEvent) {
        runWhenPerkIsEnabled(event.player) { player ->
            player.setFlyingState(true)
        }
    }

    private fun Player.setFlyingState(state: Boolean) = this.apply {
        allowFlight = state
        isFlying = state
    }

}
