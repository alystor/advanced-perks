package art.flieda.advancedperks.command.subcommands

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perk.state.PerkToggleStatus
import org.bukkit.Bukkit
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

    val perkRegistry = get<PerkRegistry>()
    val perkStateController = get<PerkStateController>()

    override fun handleCommand(commandSender: CommandSender, arguments: Array<String?>) {
        when (arguments.size) {
            1 -> {
                val perk = perkRegistry.findPerkByIdentifier(arguments[0]!!)
                if (perk == null) {
                    commandSender.sendPerkNotFound(arguments[0]!!)
                } else {
                    val result = perkStateController.togglePerk(commandSender as Player, perk)
                    commandSender.sendResultMessage(result, perk)
                }
            }

            2 -> {
                val perk = perkRegistry.findPerkByIdentifier(arguments[0]!!)
                val target = Bukkit.getPlayer(arguments[1]!!)

                when {
                    !commandSender.hasPermission(TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION) -> commandSender.sendPluginMessage(
                        "permission_denied_for_other"
                    )

                    perk == null -> commandSender.sendPerkNotFound(arguments[0]!!)

                    target == null -> commandSender.sendPluginMessage("player_not_found", "input" to arguments[1]!!)

                    else -> {
                        val result = perkStateController.forceTogglePerk(target, perk)
                        commandSender.sendResultMessage(result, perk, target)
                    }
                }
            }

            else -> commandSender.sendWrongSyntaxMessage()
        }
    }

    private fun CommandSender.sendResultMessage(result: PerkToggleStatus, perk: Perk, target: Player = this as Player) =
        when (result) {
            is PerkToggleStatus.Enabled -> sendPluginMessage(
                "result.enabled",
                "perk" to perk.getIdentifier(),
                "player" to target.name
            )

            is PerkToggleStatus.Disabled -> sendPluginMessage(
                "result.disabled",
                "perk" to perk.getIdentifier(),
                "player" to target.name
            )

            else -> {
                val reason = (result as PerkToggleStatus.Restricted).reason
                sendPluginMessage(
                    "result.restricted.${reason.name.lowercase()}",
                    "player" to target.name
                )
            }
        }

    private fun CommandSender.sendPerkNotFound(input: String) =
        sendPluginMessage("perk_not_found", "input" to input)

    override fun handleTabCompletion(
        commandSender: CommandSender,
        arguments: Array<String?>
    ): List<String?>? =
        when (arguments.size) {
            1 -> {
                perkRegistry.perkCache.values.map { it.getIdentifier() }
                    .filter { it.startsWith(arguments[0].orEmpty(), true) }
            }

            2 -> {
                if (commandSender.hasPermission(TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION)) null else emptyList()
            }

            else -> {
                emptyList()
            }
        }

}
