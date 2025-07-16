package art.flieda.advancedperks.economy.adapters

import org.bukkit.Bukkit
import org.koin.dsl.module

object EconomyAdapterModule {

    val module = module {
        single {
            when {
                Bukkit.getPluginManager().getPlugin("Vault") != null -> VaultEconomyAdapter()

                else -> NoEconomyAdapter()
            }
        }
    }

}
