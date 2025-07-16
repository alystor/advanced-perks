package art.flieda.advancedperks.economy

import art.flieda.advancedperks.economy.adapters.EconomyAdapter
import art.flieda.advancedperks.economy.adapters.EconomyTransactionResult
import org.bukkit.entity.Player

class TestEconomyAdapter : EconomyAdapter {

    var adapterMisconfigured = false
    val balanceCache = mutableMapOf<Player, Double>()
    val throwErrorForPlayer = mutableListOf<Player>()

    override fun buyPerk(player: Player, price: Double): EconomyTransactionResult = if (adapterMisconfigured) {
        EconomyTransactionResult.NO_ECONOMY_ADAPTER
    } else {
        when {
            balanceCache.getOrDefault(player, 0.0) < price -> EconomyTransactionResult.INSUFFICIENT_FUNDS

            throwErrorForPlayer.contains(player) -> EconomyTransactionResult.ERROR

            else -> {
                balanceCache.computeIfPresent(player) { _, balance ->
                    balance - price
                }
                EconomyTransactionResult.SUCCESS
            }
        }
    }

}
