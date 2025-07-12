package art.flieda.advancedperks.perk.state

sealed class PerkToggleStatus {
    object Enabled : PerkToggleStatus()
    object Disabled : PerkToggleStatus()
    object Unchanged : PerkToggleStatus()
    data class Restricted(val reason: PerkUsageRestriction) : PerkToggleStatus()
}
