package art.flieda.advancedperks.economy

sealed class PerkBuyResult {
    object Success : PerkBuyResult()
    data class Restricted(val restriction: PerkBuyRestriction) : PerkBuyResult()
}
