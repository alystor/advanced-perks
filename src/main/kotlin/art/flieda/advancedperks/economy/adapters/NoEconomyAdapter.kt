package art.flieda.advancedperks.economy.adapters

import org.bukkit.entity.Player

class NoEconomyAdapter : EconomyAdapter {

    override fun buyPerk(player: Player, price: Double): EconomyTransactionResult = EconomyTransactionResult.NO_ECONOMY_ADAPTER

}
