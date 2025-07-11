package art.flieda.advancedperks.command

import art.flieda.advancedperks.command.subcommands.TogglePerkSubCommand
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.defaultperks.BirdPerk
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object PerkCommandModule {

    val module = module {
        singleOf(::PerkCommandHandler) { createdAtStart() }

        singleOf(::TogglePerkSubCommand) bind AbstractPerkSubCommand::class
    }

}
