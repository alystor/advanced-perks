package art.flieda.advancedperks.perk.state

import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.state.data.canEnableMorePerks
import art.flieda.advancedperks.perk.state.data.getOrCreatePerkData
import art.flieda.advancedperks.perk.state.data.hasPermissionForPerk
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
            if (restriction != PerkUsageRestriction.NONE) {
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
                perk.onPerkDisable(player)
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
                perk.onPerkEnable(player)
                PerkToggleStatus.Enabled
            }
        }

    private fun getPerkUsageRestriction(player: Player, perk: Perk): PerkUsageRestriction {
        if (player.hasPermissionForPerk(perk).not()) {
            return PerkUsageRestriction.MISSING_PERMISSION
        }

        if (player.canEnableMorePerks().not()) {
            return PerkUsageRestriction.TOO_MANY_PERKS_ENABLED
        }

        if (perk.getDisallowedWorlds().any { it.equals(player.world.name, ignoreCase = true) }) {
            return PerkUsageRestriction.DISALLOWED_WORLD
        }

        return PerkUsageRestriction.NONE
    }

}

enum class PerkUsageRestriction {
    NONE,
    MISSING_PERMISSION,
    TOO_MANY_PERKS_ENABLED,
    DISALLOWED_WORLD,
}

sealed class PerkToggleStatus {
    object Enabled : PerkToggleStatus()
    object Disabled : PerkToggleStatus()
    object Unchanged : PerkToggleStatus()
    data class Restricted(val reason: PerkUsageRestriction) : PerkToggleStatus()
}
