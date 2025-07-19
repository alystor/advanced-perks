package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.EnableActionPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.entity.Enemy
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent

const val HORIZONTAL_CHECKING_DISTANCE = 20.0
const val VERTICAL_CHECKING_DISTANCE = 100.0

class MonsterAllyPerk : AbstractPerk("monster_ally"), ListenerPerk, EnableActionPerk {

    override fun onPerkEnable(player: Player) {
        player.getNearbyEntities(
            HORIZONTAL_CHECKING_DISTANCE,
            VERTICAL_CHECKING_DISTANCE,
            HORIZONTAL_CHECKING_DISTANCE
        ).filterIsInstance<Mob>().forEach { entity ->
            if (entity is Enemy && entity.target == player) {
                entity.target = null
            }
        }
    }

    @EventHandler
    fun onEntityTargetLivingEntity(event: EntityTargetLivingEntityEvent) {
        (event.target as? Player).runWhenPerkIsEnabled { player ->
            // TODO tune reason so that provocative attacks are still possible
            if (event.entity is Enemy && event.reason != EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY) {
                event.isCancelled = true
            }
        }
    }

}
