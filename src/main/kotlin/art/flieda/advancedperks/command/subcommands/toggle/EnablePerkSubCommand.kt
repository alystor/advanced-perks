package art.flieda.advancedperks.command.subcommands.toggle

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.command.CommandTarget
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perk.state.PerkToggleStatus
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.get

const val ENABLE_PERK_SUB_COMMAND_PERMISSION = "advanced_perks.command.enable"
const val ENABLE_PERK_SUB_COMMAND_OTHER_PERMISSION = "advanced_perks.command.admin.enable"

class EnablePerkSubCommand : AbstractPerkSubCommand(
    name = "enable",
    requiredPermission = listOf(
        ENABLE_PERK_SUB_COMMAND_PERMISSION,
        ENABLE_PERK_SUB_COMMAND_OTHER_PERMISSION,
    ),
) {

    val perkStateController = get<PerkStateController>()

    /* /perk enable <perk> [player] */
    override fun handleCommand(commandSender: CommandSender, arguments: Array<String>) {
        when (arguments.size) {
            1 -> {
                arguments.extractPerkAndExecute(commandSender) { perk ->
                    val result = perkStateController.enablePerk(commandSender as Player, perk)
                    commandSender.sendResultMessage(CommandTarget.SELF, result, perk)
                }
            }

            2 -> {
                if (commandSender.hasPermission(ENABLE_PERK_SUB_COMMAND_OTHER_PERMISSION).not()) {
                    commandSender.sendPluginMessage("permission_denied_for_other")
                    return
                }

                arguments.extractPerkAndPlayerAndExecute(commandSender) { perk, player ->
                    val result = perkStateController.forceEnablePerk(player, perk)
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
            is PerkToggleStatus.Unchanged -> sendPluginMessage(
                "result.${target.name.lowercase()}.unchanged",
                "perk" to perk.getIdentifier(),
                "player" to player.name
            )

            is PerkToggleStatus.Enabled -> sendPluginMessage(
                "result.${target.name.lowercase()}.enabled",
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
                    .filter { (commandSender as Player).getOrCreatePerkData().enabledPerks.contains(it).not() }
                    .filter { it.startsWith(arguments[0], true) }
            }

            2 -> {
                if (commandSender.hasPermission(ENABLE_PERK_SUB_COMMAND_OTHER_PERMISSION)) null else emptyList()
            }

            else -> {
                emptyList()
            }
        }

}
