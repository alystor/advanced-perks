package art.flieda.advancedperks.perk.types

import org.bukkit.entity.Player

interface EnableActionPerk {
    fun onPerkEnable(player: Player)
}
