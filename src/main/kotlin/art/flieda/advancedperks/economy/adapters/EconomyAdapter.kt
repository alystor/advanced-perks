package art.flieda.advancedperks.economy.adapters

import org.bukkit.entity.Player

interface EconomyAdapter {

    fun buyPerk(player: Player, price: Double): EconomyTransactionResult = EconomyTransactionResult.NO_ECONOMY_ADAPTER

}
