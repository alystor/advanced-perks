package art.flieda.advancedperks.economy

enum class PerkBuyRestriction {
    PERK_ALREADY_BOUGHT,
    INSUFFICIENT_FUNDS,
    NO_ECONOMY_ADAPTER,
    NO_PRICE_SET,
    ERROR_DURING_TRANSACTION,
}
