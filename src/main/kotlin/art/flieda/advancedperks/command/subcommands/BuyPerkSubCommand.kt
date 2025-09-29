package art.flieda.advancedperks.command.subcommands

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.economy.PerkBuyResult
import art.flieda.advancedperks.economy.PerkEconomyController
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.get

const val BUY_PERK_SUB_COMMAND_PERMISSION = "advanced_perks.command.buy"

class BuyPerkSubCommand : AbstractPerkSubCommand(
    name = "buy",
    requiredPermission = listOf(
        BUY_PERK_SUB_COMMAND_PERMISSION
    )
) {

    val perkEconomyController = get<PerkEconomyController>()

    override fun handleCommand(commandSender: CommandSender, arguments: Array<String>) =
        if (arguments.size == 1) {
            arguments.extractPerkAndExecute(commandSender) { perk ->
                val result = perkEconomyController.buyPerk(commandSender as Player, perk)
                commandSender.sendResultMessage(result, perk)
            }
        } else {
            commandSender.sendWrongSyntaxMessage()
        }

    override fun handleTabCompletion(commandSender: CommandSender, arguments: Array<String>): List<String>? =
        when (arguments.size) {
            1 -> {
                perkRegistry.perkCache.values.map { it.getIdentifier() }
                    .filterNot { (commandSender as Player).getOrCreatePerkData().boughtPerks.contains(it) }
                    .filter { it.startsWith(arguments[0], true) }
            }

            else -> {
                emptyList()
            }
        }

    private fun CommandSender.sendResultMessage(result: PerkBuyResult, perk: Perk) =
        when (result) {
            is PerkBuyResult.Success -> {
                sendPluginMessage(
                    "result.success",
                    "perk" to perk.getIdentifier(),
                    "price" to requireNotNull(perk.getPrice())
                )
            }

            is PerkBuyResult.Restricted -> {
                val restriction = result.restriction
                sendPluginMessage("result.restricted.${restriction.name.lowercase()}")
            }
        }

}
