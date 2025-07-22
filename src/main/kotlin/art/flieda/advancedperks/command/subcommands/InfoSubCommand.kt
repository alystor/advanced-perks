package art.flieda.advancedperks.command.subcommands

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.command.CommandTarget
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

const val INFO_SUB_COMMAND_PERMISSION = "advanced_perks.command.info"
const val INFO_OTHER_SUB_COMMAND_PERMISSION = "advanced_perks.command.admin.info"

class InfoSubCommand : AbstractPerkSubCommand(
    name = "info",
    requiredPermission = listOf(
        INFO_SUB_COMMAND_PERMISSION,
        INFO_OTHER_SUB_COMMAND_PERMISSION
    )
) {

    override fun handleCommand(commandSender: CommandSender, arguments: Array<String>) {
        when (arguments.size) {
            0 -> {
                commandSender.sendResultMessage(CommandTarget.SELF)
            }

            1 -> {
                if (commandSender.hasPermission(INFO_OTHER_SUB_COMMAND_PERMISSION).not()) {
                    commandSender.sendPluginMessage("permission_denied_for_other")
                    return
                }

                arguments.extractPlayerAndExecute(commandSender) { player ->
                    commandSender.sendResultMessage(CommandTarget.OTHER, player)
                }
            }

            else -> commandSender.sendWrongSyntaxMessage()
        }
    }

    private fun CommandSender.sendResultMessage(
        target: CommandTarget,
        player: Player = this as Player
    ) = player.getOrCreatePerkData().let { perkData ->
        val enabledPerks = perkData.enabledPerks.joinToString(" ").takeIf { it.isNotBlank() } ?: "-"
        val boughtPerks = perkData.boughtPerks.joinToString(" ").takeIf { it.isNotBlank() } ?: "-"
        sendPluginMessageList(
            target.toString().lowercase(),
            "player" to player.name,
            "enabled_perks" to enabledPerks,
            "bought_perks" to boughtPerks
        )
    }

    override fun handleTabCompletion(commandSender: CommandSender, arguments: Array<String>): List<String>? =
        when (arguments.size) {
            1 -> if(commandSender.hasPermission(INFO_OTHER_SUB_COMMAND_PERMISSION)) null else emptyList()
            else -> emptyList()
        }

}