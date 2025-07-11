package art.flieda.advancedperks.command

import art.flieda.advancedperks.PLUGIN_COMMAND_ALIAS
import art.flieda.advancedperks.core.configuration.MessageConfiguration
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.koin.core.component.KoinComponent
import org.koin.core.component.get


class PerkCommandHandler : CommandExecutor, TabCompleter, KoinComponent {

    init {
        val command = Bukkit.getServer().getPluginCommand(PLUGIN_COMMAND_ALIAS) ?: throw IllegalStateException(
            """
            The plugins command wasn't found by the alias "$PLUGIN_COMMAND_ALIAS".
            This either means that the plugin clashes with another plugin that uses the same command alias.
            Or that the jar was tempered with and the command was removed from the plugin.yml.
        """.trimIndent()
        )
        command.setExecutor(this)
        command.tabCompleter = this
    }

    private val messageConfig = get<MessageConfiguration>()

    private val subCommands = getKoin().getAll<AbstractPerkSubCommand>().flatMap { command ->
        listOf(command.name.lowercase() to command) + command.aliases.map { it.lowercase() to command }
    }.associate { it.first to it.second }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String?>): Boolean =
        args.firstOrNull()?.let { subCommands[it.lowercase()] }.let { subCommand ->
            when {
                subCommand == null -> false

                !sender.hasPermissionForSubCommand(subCommand) -> {
                    sender.sendMessage(messageConfig.getPermissionDeniedMessage())
                    false
                }

                else -> {
                    subCommand.handleCommand(sender, args.drop(1).toTypedArray())
                    true
                }
            }
        }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>
    ): List<String?>? {
        if (args.isEmpty()) {
            return emptyList()
        }

        val subCommand = subCommands[args.first()!!.lowercase()]

        return if (subCommand != null) {
            if (sender.hasPermissionForSubCommand(subCommand)) {
                emptyList()
            } else {
                subCommand.handleTabCompletion(sender, args.drop(1).toTypedArray())
            }
        } else {
            subCommands.values.filter { subCommand ->
                sender.hasPermissionForSubCommand(subCommand) && subCommand.name.lowercase()
                    .startsWith(args.first()!!.lowercase())
            }.map { it.name }
        }
    }

    private fun CommandSender.hasPermissionForSubCommand(subCommand: AbstractPerkSubCommand): Boolean =
        subCommand.requiredPermission == null || this.hasPermission(subCommand.requiredPermission)

}
