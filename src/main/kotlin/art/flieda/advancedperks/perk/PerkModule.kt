package art.flieda.advancedperks.perk

import art.flieda.advancedperks.perk.defaultperks.BirdPerk
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object PerkModule {

    val module = module {
        singleOf(::BirdPerk) bind Perk::class
    }

}