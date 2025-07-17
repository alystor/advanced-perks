package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.event.EventHandler
import org.bukkit.event.entity.PlayerDeathEvent

class KeepInventoryPerk : AbstractPerk("keep_inventory"), ListenerPerk {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.entity.runWhenPerkIsEnabled {
            event.keepInventory = true
            event.drops.clear()
        }
    }

}
