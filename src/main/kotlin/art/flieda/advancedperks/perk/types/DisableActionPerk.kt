package art.flieda.advancedperks.perk.types

import org.bukkit.entity.Player

interface DisableActionPerk {
    fun onPerkDisable(player: Player)
}
