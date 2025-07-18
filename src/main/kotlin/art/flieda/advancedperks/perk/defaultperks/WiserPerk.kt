package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent

class WiserPerk : AbstractPerk("wiser"), ListenerPerk {

    //TODO test functionality in a live environment
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.entity.runWhenPerkIsEnabled {
            event.keepLevel = true
            event.droppedExp = 0
        }
    }

}
