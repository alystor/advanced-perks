package art.flieda.advancedperks.perk.state

import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.state.data.canEnableMorePerks
import art.flieda.advancedperks.perk.state.data.getOrCreatePerkData
import art.flieda.advancedperks.perk.state.data.hasPermissionForPerk
import art.flieda.advancedperks.perk.types.DisableActionPerk
import art.flieda.advancedperks.perk.types.EnableActionPerk
import org.bukkit.entity.Player

class PerkStateController {

    fun forceTogglePerk(player: Player, perk: Perk): PerkToggleStatus =
        if (player.getOrCreatePerkData().hasPerkEnabled(perk)) {
            forceDisablePerk(player, perk)
        } else {
            forceEnablePerk(player, perk)
        }

    fun togglePerk(player: Player, perk: Perk): PerkToggleStatus =
        if (player.getOrCreatePerkData().hasPerkEnabled(perk)) {
            disablePerk(player, perk)
        } else {
            enablePerk(player, perk)
        }

    fun disablePerk(player: Player, perk: Perk): PerkToggleStatus =
        getPerkUsageRestriction(player, perk).let { restriction ->
            if (restriction == PerkUsageRestriction.MISSING_PERMISSION) {
                PerkToggleStatus.Restricted(restriction)
            } else {
                forceDisablePerk(player, perk)
            }
        }

    fun enablePerk(player: Player, perk: Perk): PerkToggleStatus =
        getPerkUsageRestriction(player, perk).let { restriction ->
            if (restriction != PerkUsageRestriction.NONE) {
                PerkToggleStatus.Restricted(restriction)
            } else {
                forceEnablePerk(player, perk)
            }
        }

    fun forceDisablePerk(player: Player, perk: Perk): PerkToggleStatus =
        player.getOrCreatePerkData().let { perkData ->
            if (!(perkData.enabledPerks.contains(perk.getIdentifier()))) {
                PerkToggleStatus.Unchanged
            } else {
                //TODO add perk toggle event
                perkData.enabledPerks.remove(perk.getIdentifier())
                if (perk is DisableActionPerk) {
                    perk.onPerkDisable(player)
                }
                PerkToggleStatus.Disabled
            }
        }

    fun forceEnablePerk(player: Player, perk: Perk): PerkToggleStatus =
        player.getOrCreatePerkData().let { perkData ->
            if (perkData.hasPerkEnabled(perk)) {
                PerkToggleStatus.Unchanged
            } else {
                //TODO add perk toggle event
                perkData.enabledPerks.add(perk.getIdentifier())
                if (perk is EnableActionPerk) {
                    perk.onPerkEnable(player)
                }
                PerkToggleStatus.Enabled
            }
        }

    private fun getPerkUsageRestriction(player: Player, perk: Perk): PerkUsageRestriction =
        when {
            player.hasPermissionForPerk(perk).not() -> PerkUsageRestriction.MISSING_PERMISSION
            player.canEnableMorePerks().not() -> PerkUsageRestriction.TOO_MANY_PERKS_ENABLED
            perk.getDisallowedWorlds()
                .any { it.equals(player.world.name, ignoreCase = true) } -> PerkUsageRestriction.DISALLOWED_WORLD

            else -> PerkUsageRestriction.NONE
        }

}
