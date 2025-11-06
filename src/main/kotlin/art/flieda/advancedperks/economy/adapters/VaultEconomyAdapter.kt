package art.flieda.advancedperks.economy.adapters

import art.flieda.advancedperks.AdvancedPerks.logger
import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse.ResponseType
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class VaultEconomyAdapter : EconomyAdapter {

    private val vaultEconomy = runCatching {
        requireNotNull(Bukkit.getServer().servicesManager.getRegistration(Economy::class.java)).provider
    }.onFailure {
        logger.warning("Vault economy provider is not available. Please install Vault and an compatible Economy plugin.")
    }.getOrNull()

    override fun buyPerk(player: Player, price: Double): EconomyTransactionResult = vaultEconomy?.let { economy ->
        when {
            economy.getBalance(player) < price -> {
                EconomyTransactionResult.INSUFFICIENT_FUNDS
            }

            else -> {
                val result = economy.withdrawPlayer(player, price)

                when (result.type) {
                    ResponseType.NOT_IMPLEMENTED, ResponseType.FAILURE -> {
                        logger.warning { "An error occurred during the withdraw of the funds to buy the perk: ${result.errorMessage}" }
                        EconomyTransactionResult.ERROR
                    }

                    ResponseType.SUCCESS -> {
                        EconomyTransactionResult.SUCCESS
                    }
                }
            }
        }
    } ?: EconomyTransactionResult.NO_ECONOMY_ADAPTER

}
