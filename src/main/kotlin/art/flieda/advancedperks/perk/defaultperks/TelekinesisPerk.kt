package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockDropItemEvent

class TelekinesisPerk : AbstractPerk("telekinesis"), ListenerPerk {

    //TODO test functionality in a live environment
    @EventHandler
    fun onBlockDropItem(event: BlockDropItemEvent) {
        event.player.runWhenPerkIsEnabled { player ->
            event.items.removeIf { item ->
                val remaining = player.inventory.addItem(item.itemStack)
                remaining.isEmpty()
            }
        }
    }

}
