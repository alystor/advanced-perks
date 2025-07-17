package art.flieda.advancedperks.perk

import art.flieda.advancedperks.perk.defaultperks.BirdPerk
import art.flieda.advancedperks.perk.defaultperks.ChickenPerk
import art.flieda.advancedperks.perk.defaultperks.DolphinPerk
import art.flieda.advancedperks.perk.defaultperks.FarmerPerk
import art.flieda.advancedperks.perk.defaultperks.KeepInventoryPerk
import art.flieda.advancedperks.perk.defaultperks.SmelterPerk
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object PerkModule {

    val module = module {
        singleOf(::BirdPerk) bind Perk::class
        singleOf(::ChickenPerk) bind Perk::class
        singleOf(::DolphinPerk) bind Perk::class
        singleOf(::FarmerPerk) bind Perk::class
        singleOf(::KeepInventoryPerk) bind Perk::class
        singleOf(::SmelterPerk) bind Perk::class
    }

}
