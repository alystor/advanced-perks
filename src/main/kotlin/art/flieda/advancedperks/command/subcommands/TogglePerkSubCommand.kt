package art.flieda.advancedperks.command.subcommands

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perk.state.PerkToggleStatus
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.get

class TogglePerkSubCommand : AbstractPerkSubCommand(
    name = "toggle",
    requiredPermission = "advanced_perks.command.toggle",
) {

    val perkRegistry = get<PerkRegistry>()
    val perkStateController = get<PerkStateController>()

    override fun handleCommand(commandSender: CommandSender, arguments: Array<String?>) {
        when {
            arguments.size in 1..2 -> {
                val perk = perkRegistry.findPerkByIdentifier(arguments[0]!!)
                if (perk == null) {
                    commandSender.sendPluginMessage("perk_not_found", "input" to arguments[0]!!)
                    return
                }

                val target = if (arguments.size == 1) commandSender as Player else Bukkit.getPlayer(arguments[1]!!)
                if (target == null) {
                    commandSender.sendPluginMessage("player_not_found", "input" to arguments[1]!!)
                    return
                }
                
                val result = perkStateController.togglePerk(target, perk)

                when (result) {
                    is PerkToggleStatus.Enabled -> commandSender.sendPluginMessage(
                        "result.enabled",
                        "perk" to perk.getIdentifier(),
                        "player" to target.name
                    )

                    is PerkToggleStatus.Disabled -> commandSender.sendPluginMessage(
                        "result.disabled",
                        "perk" to perk.getIdentifier(),
                        "player" to target.name
                    )

                    else -> {
                        val reason = (result as PerkToggleStatus.Restricted).reason
                        commandSender.sendPluginMessage(
                            "result.restricted.${reason.name.lowercase()}",
                            "player" to target.name
                        )
                    }
                }
            }

            else -> commandSender.sendWrongSyntaxMessage()
        }
    }

    override fun handleTabCompletion(
        commandSender: CommandSender,
        arguments: Array<String?>
    ): List<String?>? =
        when (arguments.size) {
            1 -> {
                perkRegistry.perkCache.values.map { it.getIdentifier() }
                    .filter { it.startsWith(arguments[0] ?: "", true) }
            }

            2 -> {
                // null means it will return online players
                null
            }

            else -> {
                emptyList()
            }
        }

}
