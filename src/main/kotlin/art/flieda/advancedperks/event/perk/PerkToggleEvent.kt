package art.flieda.advancedperks.event.perk

import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.state.PerkToggleStatus
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable

open class PerkToggleEvent(
    player: Player,
    perk: Perk,
    private val status: PerkToggleStatus,
) : PlayerPerkEvent(player, perk), Cancellable {

    var eventCancelled = false

    fun getStatus(): PerkToggleStatus = status

    override fun isCancelled(): Boolean = eventCancelled

    override fun setCancelled(cancel: Boolean) {
        eventCancelled = cancel
    }

}
