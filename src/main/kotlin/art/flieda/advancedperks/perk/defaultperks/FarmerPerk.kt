package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.utilities.runTaskSyncLater
import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.block.data.Ageable
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.Plugin
import org.koin.core.component.get

class FarmerPerk : AbstractPerk("farmer"), ListenerPerk {

    val plugin = get<Plugin>()

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        event.player.runWhenPerkIsEnabled { player ->
            (event.block.blockData as? Ageable)?.let { ageable ->
                if (ageable.age == ageable.maximumAge) {
                    val crop = event.block.type
                    runTaskSyncLater(1) {
                        event.block.type = crop
                    }
                }
            }
        }
    }

}
