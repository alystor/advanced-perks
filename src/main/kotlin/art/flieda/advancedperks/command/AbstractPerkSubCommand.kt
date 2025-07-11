package art.flieda.advancedperks.command

import org.bukkit.command.CommandSender

abstract class AbstractPerkSubCommand(
    val name: String,
    val aliases: List<String> = emptyList(),
    val requiredPermission: String?,
) {

    abstract fun handleCommand(commandSender: CommandSender, arguments: Array<String?>)

    abstract fun handleTabCompletion(commandSender: CommandSender, arguments: Array<String?>): List<String?>
 
}
