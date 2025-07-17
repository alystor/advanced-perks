package art.flieda.advancedperks.command

import art.flieda.advancedperks.command.subcommands.BuyPerkSubCommand
import art.flieda.advancedperks.command.subcommands.DeletePropertySubCommand
import art.flieda.advancedperks.command.subcommands.SetPropertySubCommand
import art.flieda.advancedperks.command.subcommands.toggle.DisablePerkSubCommand
import art.flieda.advancedperks.command.subcommands.toggle.EnablePerkSubCommand
import art.flieda.advancedperks.command.subcommands.toggle.TogglePerkSubCommand
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

object PerkCommandModule {

    val module = module {
        singleOf(::PerkCommandHandler) { createdAtStart() }

        singleOf(::TogglePerkSubCommand) bind AbstractPerkSubCommand::class
        singleOf(::BuyPerkSubCommand) bind AbstractPerkSubCommand::class
        singleOf(::DisablePerkSubCommand) bind AbstractPerkSubCommand::class
        singleOf(::EnablePerkSubCommand) bind AbstractPerkSubCommand::class
        singleOf(::SetPropertySubCommand) bind AbstractPerkSubCommand::class
        singleOf(::DeletePropertySubCommand) bind AbstractPerkSubCommand::class
    }

}
