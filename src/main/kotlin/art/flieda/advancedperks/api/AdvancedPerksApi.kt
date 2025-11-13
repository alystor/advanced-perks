package art.flieda.advancedperks.api

import art.flieda.advancedperks.api.placeholderapi.expansions.CanPerkBeUsedExpansion
import art.flieda.advancedperks.api.placeholderapi.expansions.PerkEnabledExpansion
import art.flieda.advancedperks.api.placeholderapi.expansions.PerkLimitExpansion
import org.bukkit.Bukkit
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

class AdvancedPerksApi private constructor() {

    companion object {

        val module = module {
            if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                singleOf(::CanPerkBeUsedExpansion) { createdAtStart() }
                singleOf(::PerkEnabledExpansion) { createdAtStart() }
                singleOf(::PerkLimitExpansion) { createdAtStart() }
            }
        }

    }

}
