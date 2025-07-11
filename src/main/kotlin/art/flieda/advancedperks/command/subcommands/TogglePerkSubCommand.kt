package art.flieda.advancedperks.command.subcommands

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.core.configuration.MessageConfiguration
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent

class TogglePerkSubCommand(
    private val messageConfig: MessageConfiguration,
) : AbstractPerkSubCommand(
    name = "toggle",
    requiredPermission = "advanced_perks.command.toggle",
), KoinComponent {

    override fun handleCommand(commandSender: CommandSender, arguments: Array<String?>) {
        //TODO IMPLEMENT
    }

    override fun handleTabCompletion(
        commandSender: CommandSender,
        arguments: Array<String?>
    ): List<String?> {
        return emptyList()
    }

}
