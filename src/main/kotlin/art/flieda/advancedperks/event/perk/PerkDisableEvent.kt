package art.flieda.advancedperks.event.perk

import art.flieda.advancedperks.perk.Perk
import org.bukkit.entity.Player

class PerkDisableEvent(
    player: Player,
    perk: Perk,
) : PlayerPerkEvent(
    player = player,
    perk = perk,
)
