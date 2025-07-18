package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.entity.Enemy
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityTargetEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent

// TODO test in a live environment
class MonsterAllyPerk : AbstractPerk("monster_ally"), ListenerPerk {

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
