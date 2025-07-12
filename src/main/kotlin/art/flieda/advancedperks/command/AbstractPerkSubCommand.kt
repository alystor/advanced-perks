package art.flieda.advancedperks.command

import art.flieda.advancedperks.core.configuration.MessageConfiguration
import org.bukkit.command.CommandSender
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

abstract class AbstractPerkSubCommand(
    val name: String,
    val aliases: List<String> = emptyList(),
    val requiredPermission: List<String> = emptyList(),
) : KoinComponent {

    val messageConfiguration = get<MessageConfiguration>()

    abstract fun handleCommand(commandSender: CommandSender, arguments: Array<String?>)

    abstract fun handleTabCompletion(commandSender: CommandSender, arguments: Array<String?>): List<String?>?

    internal fun CommandSender.sendPluginMessage(key: String, vararg placeHolders: Pair<String, Any>) =
        this.sendMessage(
            messageConfiguration.getMessage(
                "command.${this@AbstractPerkSubCommand.name}.$key",
                *placeHolders
            )
        )

    internal fun CommandSender.sendWrongSyntaxMessage() = sendPluginMessage("wrong_syntax")

}


