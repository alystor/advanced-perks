package art.flieda.advancedperks.perk.state

import art.flieda.advancedperks.event.perk.PerkDisableEvent
import art.flieda.advancedperks.event.perk.PerkEnableEvent
import art.flieda.advancedperks.event.perk.PerkToggleEvent
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.types.DisableActionPerk
import art.flieda.advancedperks.perk.types.EffectPerk
import art.flieda.advancedperks.perk.types.EnableActionPerk
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import art.flieda.advancedperks.perkdata.hasPermissionToUsePerk
import art.flieda.advancedperks.perkdata.isAllowedToEnableMorePerks
import org.bukkit.Bukkit
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
        getPerkDisableUsageRestriction(player, perk).let { restriction ->
            if (restriction != PerkUsageRestriction.NONE) {
                PerkToggleStatus.Restricted(restriction)
            } else {
                forceDisablePerk(player, perk)
            }
        }

    fun enablePerk(player: Player, perk: Perk): PerkToggleStatus =
        getPerkEnableUsageRestriction(player, perk).let { restriction ->
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
                val toggleEvent = PerkToggleEvent(
                    player = player,
                    perk = perk,
                    status = PerkToggleStatus.Disabled
                )
                Bukkit.getPluginManager().callEvent(toggleEvent)
                if (toggleEvent.isCancelled) {
                    return PerkToggleStatus.CancelledByEvent
                }

                perkData.enabledPerks.remove(perk.getIdentifier())
                if (perk is EffectPerk) {
                    perk.getPotionEffectsToApply().forEach { potionEffect ->
                        player.removePotionEffect(potionEffect.type)
                    }
                }

                if (perk is DisableActionPerk) {
                    perk.onPerkDisable(player)
                }

                val disableEvent = PerkDisableEvent(
                    player = player,
                    perk = perk
                )
                Bukkit.getPluginManager().callEvent(disableEvent)

                PerkToggleStatus.Disabled
            }
        }

    fun forceEnablePerk(player: Player, perk: Perk): PerkToggleStatus =
        player.getOrCreatePerkData().let { perkData ->
            if (perkData.hasPerkEnabled(perk)) {
                PerkToggleStatus.Unchanged
            } else {
                val toggleEvent = PerkToggleEvent(
                    player = player,
                    perk = perk,
                    status = PerkToggleStatus.Enabled
                )
                Bukkit.getPluginManager().callEvent(toggleEvent)
                if (toggleEvent.isCancelled) {
                    return PerkToggleStatus.CancelledByEvent
                }

                perkData.enabledPerks.add(perk.getIdentifier())
                if (perk is EffectPerk) {
                    player.addPotionEffects(perk.getPotionEffectsToApply())
                }

                if (perk is EnableActionPerk) {
                    perk.onPerkEnable(player)
                }
                val enableEvent = PerkEnableEvent(
                    player = player,
                    perk = perk
                )
                Bukkit.getPluginManager().callEvent(enableEvent)

                PerkToggleStatus.Enabled
            }
        }

    private fun getPerkEnableUsageRestriction(player: Player, perk: Perk): PerkUsageRestriction =
        when {
            player.hasPermissionToUsePerk(perk).not() -> PerkUsageRestriction.MISSING_PERMISSION
            player.isAllowedToEnableMorePerks().not() -> PerkUsageRestriction.TOO_MANY_PERKS_ENABLED
            perk.getDisallowedWorlds()
                .any { it.equals(player.world.name, ignoreCase = true) } -> PerkUsageRestriction.DISALLOWED_WORLD

            else -> PerkUsageRestriction.NONE
        }

    private fun getPerkDisableUsageRestriction(player: Player, perk: Perk): PerkUsageRestriction =
        when {
            player.hasPermissionToUsePerk(perk).not() -> PerkUsageRestriction.MISSING_PERMISSION

            else -> PerkUsageRestriction.NONE
        }

}
