package art.flieda.advancedperks.api.placeholderapi.expansions

import art.flieda.advancedperks.api.placeholderapi.AbstractAdvancedPerksExpansion
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perk.state.PerkUsageRestriction
import org.bukkit.entity.Player
import org.koin.core.component.get

private const val EXPANSION_IDENTIFIER = "advancedperks.use"

class CanPerkBeUsedExpansion : AbstractAdvancedPerksExpansion() {

    val perkRegistry: PerkRegistry = get<PerkRegistry>()
    val perkStateController: PerkStateController = get<PerkStateController>()

    override fun onPlaceholderRequest(player: Player, params: String): String? =
        perkRegistry.findPerkByIdentifier(params)?.let { perk ->
            (perkStateController.getPerkEnableUsageRestriction(player, perk) == PerkUsageRestriction.NONE).toString()
        } ?: "false"

    override fun getIdentifier(): String = EXPANSION_IDENTIFIER

}
