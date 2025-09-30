package art.flieda.advancedperks.command.subcommands

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.guisystem.GuiSystemService
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.get

const val GUI_SUB_COMMAND_PERMISSION = "advanced_perks.command.gui"
const val GUI_SUB_COMMAND_OTHER_PERMISSION = "advanced_perks.command.admin.gui"

class GuiSubCommand : AbstractPerkSubCommand(
    name = "gui",
    requiredPermission = listOf(
        GUI_SUB_COMMAND_PERMISSION
    )
) {

    val guiSystemService = get<GuiSystemService>()

    /* /perk gui [player] */
    override fun handleCommand(commandSender: CommandSender, arguments: Array<String>) {
        when (arguments.size) {
            0 -> {
                (commandSender as? Player)?.let { player ->
                    guiSystemService.getPerkGuiOfPlayer(player).openGuiWindow(player)
                }
            }

            1 -> {
                if (commandSender.hasPermission(GUI_SUB_COMMAND_OTHER_PERMISSION).not()) {
                    commandSender.sendPluginMessage("permission_denied_for_other")
                    return
                }

                arguments.extractPlayerAndExecute(commandSender) { player ->
                    guiSystemService.getPerkGuiOfPlayer(player).openGuiWindow(commandSender as Player)
                }
            }

            else -> {
                commandSender.sendWrongSyntaxMessage()
            }
        }


    }

    override fun handleTabCompletion(commandSender: CommandSender, arguments: Array<String>): List<String>? =
        when {
            arguments.size <= 1 -> null
            else -> emptyList()
        }

}
