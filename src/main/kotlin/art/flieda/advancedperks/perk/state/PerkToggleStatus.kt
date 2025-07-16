package art.flieda.advancedperks.perk.state

sealed class PerkToggleStatus {
    object Enabled : PerkToggleStatus()
    object Disabled : PerkToggleStatus()
    object Unchanged : PerkToggleStatus()
    object CancelledByEvent : PerkToggleStatus()
    data class Restricted(val reason: PerkUsageRestriction) : PerkToggleStatus()
}
