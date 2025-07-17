package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityDamageEvent

class ChickenPerk : AbstractPerk("chicken"), ListenerPerk {

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        (event.entity as? Player).runWhenPerkIsEnabled { player ->
            event.isCancelled = event.cause == EntityDamageEvent.DamageCause.FALL
        }
    }

}
