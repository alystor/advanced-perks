package art.flieda.advancedperks.event.perk

import art.flieda.advancedperks.event.AdvancedPerksEvent
import art.flieda.advancedperks.perk.Perk
import org.bukkit.entity.Player

open class PlayerPerkEvent(
    private val player: Player,
    private val perk: Perk,
) : AdvancedPerksEvent() {

    fun getPlayer(): Player = player

    fun getPerk(): Perk = perk

}
