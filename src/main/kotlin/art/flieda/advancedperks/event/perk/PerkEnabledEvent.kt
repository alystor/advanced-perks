package art.flieda.advancedperks.event.perk

import art.flieda.advancedperks.perk.Perk
import org.bukkit.entity.Player

class PerkEnabledEvent(
    player: Player,
    perk: Perk,
) : PlayerPerkEvent(
    player = player,
    perk = perk,
)
