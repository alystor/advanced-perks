package art.flieda.advancedperks.command

import art.flieda.advancedperks.configuration.MessageConfiguration
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.registry.PerkRegistry
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

abstract class AbstractPerkSubCommand(
    val name: String,
    val aliases: List<String> = emptyList(),
    val requiredPermission: List<String> = emptyList(),
) : KoinComponent {

    val messageConfiguration = get<MessageConfiguration>()
    val perkRegistry = get<PerkRegistry>()

    abstract fun handleCommand(commandSender: CommandSender, arguments: Array<String>)

    abstract fun handleTabCompletion(commandSender: CommandSender, arguments: Array<String>): List<String>?

    internal fun CommandSender.sendPluginMessage(key: String, vararg placeHolders: Pair<String, Any>) =
        this.sendMessage(
            messageConfiguration.getMessage(
                "command.${this@AbstractPerkSubCommand.name}.$key", *placeHolders
            )
        )

    internal fun Array<String>.extractPerkAndPlayerAndExecute(
        commandSender: CommandSender, action: (Perk, Player) -> Unit
    ) {
        if (this.size == 2) {
            val perk = perkRegistry.findPerkByIdentifier(this[0])
            if (perk == null) {
                commandSender.sendPerkNotFound(this[0])
                return
            }
            val player = Bukkit.getPlayer(this[1])
            if (player == null) {
                commandSender.sendPluginMessage("player_not_found", "input" to this[1])
                return
            }

            action(perk, player)
        }
    }

    internal fun Array<String>.extractPerkAndExecute(
        commandSender: CommandSender, action: (Perk) -> Unit
    ) {
        if (this.size == 1) {
            val perk = perkRegistry.findPerkByIdentifier(this[0])
            if (perk == null) {
                commandSender.sendPerkNotFound(this[0])
                return
            }

            action(perk)
        }
    }

    internal fun CommandSender.sendWrongSyntaxMessage() = sendPluginMessage("wrong_syntax")
    internal fun CommandSender.sendPerkNotFound(input: String) = sendPluginMessage("perk_not_found", "input" to input)
}


