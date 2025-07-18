package art.flieda.advancedperks.compatibility

import art.flieda.advancedperks.compatibility.bukkit.ChangeWorldPerkCompatibilityHandler
import art.flieda.advancedperks.compatibility.bukkit.PotionDeSyncCompatibilityHandler
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object CompatibilityModule {

    val module = module(createdAtStart = true) {
        singleOf(::PotionDeSyncCompatibilityHandler)
        singleOf(::ChangeWorldPerkCompatibilityHandler)
    }

}
