package art.flieda.advancedperks.economy

import art.flieda.advancedperks.economy.adapters.EconomyAdapter
import art.flieda.advancedperks.economy.adapters.EconomyTransactionResult
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import org.bukkit.entity.Player

class PerkEconomyController(
    private val economyAdapter: EconomyAdapter
) {

    fun buyPerk(player: Player, perk: Perk): PerkBuyResult =
        perk.getPrice()?.let { price ->
            when {
                player.getOrCreatePerkData()
                    .hasPerkBought(perk) -> PerkBuyResult.Restricted(PerkBuyRestriction.PERK_ALREADY_BOUGHT)

                else -> {
                    when (economyAdapter.buyPerk(player, price)) {
                        EconomyTransactionResult.ERROR -> PerkBuyResult.Restricted(PerkBuyRestriction.ERROR_DURING_TRANSACTION)
                        EconomyTransactionResult.NO_ECONOMY_ADAPTER -> PerkBuyResult.Restricted(PerkBuyRestriction.NO_ECONOMY_ADAPTER)
                        EconomyTransactionResult.INSUFFICIENT_FUNDS -> PerkBuyResult.Restricted(PerkBuyRestriction.INSUFFICIENT_FUNDS)
                        EconomyTransactionResult.SUCCESS -> {
                            player.getOrCreatePerkData().boughtPerks.add(perk.getIdentifier())
                            PerkBuyResult.Success
                        }
                    }
                }
            }
        } ?: PerkBuyResult.Restricted(PerkBuyRestriction.NO_PRICE_SET)

}
