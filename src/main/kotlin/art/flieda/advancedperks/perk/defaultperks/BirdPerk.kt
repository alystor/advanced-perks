package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerToggleFlightEvent

class BirdPerk : AbstractPerk("bird") {

    init {
        println(properties.toString())
    }

    @EventHandler
    fun test(event: PlayerToggleFlightEvent) {
        val player = event.player
        runWhenPerkIsEnabled(player) {
        }
    }

}
