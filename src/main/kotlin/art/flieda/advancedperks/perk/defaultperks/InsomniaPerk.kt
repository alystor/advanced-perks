package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.entity.Phantom
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.EntityTargetLivingEntityEvent

// TODO test in a live environment
class InsomniaPerk : AbstractPerk("insomnia"), ListenerPerk {

    @EventHandler
    fun onEntityTargetLivingEntity(event: EntityTargetLivingEntityEvent) {
        (event.target as? Player).runWhenPerkIsEnabled { player ->
            // TODO check the possibility for finer targeting of the correct phantom with getReason()
            if(event.entity is Phantom) {
                event.entity.remove()
            }
        }
    }

}
