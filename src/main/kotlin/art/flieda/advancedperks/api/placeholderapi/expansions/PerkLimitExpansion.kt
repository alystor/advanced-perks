package art.flieda.advancedperks.api.placeholderapi.expansions

import art.flieda.advancedperks.api.placeholderapi.AbstractAdvancedPerksExpansion
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import org.bukkit.entity.Player

private const val EXPANSION_IDENTIFIER = "advancedperks.perk-limit"

class PerkLimitExpansion : AbstractAdvancedPerksExpansion() {

    override fun onPlaceholderRequest(player: Player, params: String): String? = player.getOrCreatePerkData()
        .getMaxActivePerksAllowed()
        .toString()

    override fun getIdentifier(): String = EXPANSION_IDENTIFIER

}
