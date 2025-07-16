package art.flieda.advancedperks.command.subcommands.toggle

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.command.CommandTarget
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perk.state.PerkToggleStatus
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.get

const val TOGGLE_PERK_SUB_COMMAND_PERMISSION = "advanced_perks.command.toggle"
const val TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION = "advanced_perks.command.admin.toggle"

class TogglePerkSubCommand : AbstractPerkSubCommand(
    name = "toggle",
    requiredPermission = listOf(
        TOGGLE_PERK_SUB_COMMAND_PERMISSION,
        TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION,
    ),
) {

    val perkStateController = get<PerkStateController>()

    override fun handleCommand(commandSender: CommandSender, arguments: Array<String>) {
        when (arguments.size) {
            1 -> {
                arguments.extractPerkAndExecute(commandSender) { perk ->
                    val result = perkStateController.togglePerk(commandSender as Player, perk)
                    commandSender.sendResultMessage(CommandTarget.SELF, result, perk)
                }
            }

            2 -> {
                if (commandSender.hasPermission(TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION).not()) {
                    commandSender.sendPluginMessage("permission_denied_for_other")
                    return
                }

                arguments.extractPerkAndPlayerAndExecute(commandSender) { perk, player ->
                    val result = perkStateController.forceTogglePerk(player, perk)
                    commandSender.sendResultMessage(CommandTarget.OTHER, result, perk, player)
                }
            }

            else -> commandSender.sendWrongSyntaxMessage()
        }
    }

    private fun CommandSender.sendResultMessage(
        target: CommandTarget,
        result: PerkToggleStatus,
        perk: Perk,
        player: Player = this as Player
    ) =
        when (result) {
            is PerkToggleStatus.Enabled -> sendPluginMessage(
                "result.${target.name.lowercase()}.enabled",
                "perk" to perk.getIdentifier(),
                "player" to player.name
            )

            is PerkToggleStatus.Disabled -> sendPluginMessage(
                "result.${target.name.lowercase()}.disabled",
                "perk" to perk.getIdentifier(),
                "player" to player.name
            )

            else -> {
                val reason = (result as PerkToggleStatus.Restricted).reason
                sendPluginMessage(
                    "result.${target.name.lowercase()}.restricted.${reason.name.lowercase()}",
                    "player" to player.name
                )
            }
        }

    override fun handleTabCompletion(
        commandSender: CommandSender,
        arguments: Array<String>
    ): List<String>? =
        when (arguments.size) {
            1 -> {
                perkRegistry.perkCache.values.map { it.getIdentifier() }
                    .filter { it.startsWith(arguments[0], true) }
            }

            2 -> {
                if (commandSender.hasPermission(TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION)) null else emptyList()
            }

            else -> {
                emptyList()
            }
        }

}
