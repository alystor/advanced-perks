package art.flieda.advancedperks.command.subcommands

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.guisystem.GuiSystemService
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.get

const val GUI_SUB_COMMAND_PERMISSION = "advanced_perks.command.gui"

class GuiSubCommand : AbstractPerkSubCommand(
    name = "gui",
    requiredPermission = listOf(
        GUI_SUB_COMMAND_PERMISSION
    )
) {

    val guiSystemService = get<GuiSystemService>()

    override fun handleCommand(commandSender: CommandSender, arguments: Array<String>) {
        (commandSender as? Player)?.let { player ->
            guiSystemService.openPerkGuiForPlayer(player)
        }
    }

    override fun handleTabCompletion(commandSender: CommandSender, arguments: Array<String>): List<String>? =
        emptyList()

}
