package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerToggleSneakEvent

private const val VERTICAL_BOOST_MULTIPLIER = 1.5
private const val DIRECTION_BOOST_MULTIPLIER = 0.5

class DolphinPerk : AbstractPerk("dolphin"), ListenerPerk {

    @EventHandler
    fun onPlayerToggleSneak(event: PlayerToggleSneakEvent) {
        event.player.runWhenPerkIsEnabled { player ->
            if (event.isSneaking && player.location.block.type == Material.WATER) {
                val directionVector = player.location.direction.clone()
                player.velocity = directionVector.multiply(DIRECTION_BOOST_MULTIPLIER)
                    .setY(directionVector.y.times(VERTICAL_BOOST_MULTIPLIER))
            }
        }
    }

}
