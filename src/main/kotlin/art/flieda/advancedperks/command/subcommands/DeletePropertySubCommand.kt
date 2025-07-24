package art.flieda.advancedperks.command.subcommands

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.properties.PerkProperty
import org.bukkit.command.CommandSender

const val DELETE_PROPERTY_SUB_COMMAND_PERMISSION = "advanced_perks.command.admin.delete_property"

/* /perks delete_property <perk> <property> */
class DeletePropertySubCommand : AbstractPerkSubCommand(
    name = "delete_property",
    requiredPermission = listOf(DELETE_PROPERTY_SUB_COMMAND_PERMISSION)
) {
    override fun handleCommand(
        commandSender: CommandSender,
        arguments: Array<String>
    ) = if (arguments.size == 2) {
        arguments.extractPerkAndPerkPropertyAndExecute(commandSender) { perk, property ->
            val result = perk.deleteProperty(property)
            commandSender.sendPluginMessage(
                "result.${result.name.lowercase()}",
                "perk" to perk.getIdentifier(),
                "property" to property.configurationKey,
            )
        }
    } else {
        commandSender.sendWrongSyntaxMessage()
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
                PerkProperty.entries.filter { it.mandatory.not() && it.modifiable }
                    .map { it.configurationKey }
                    .filter { it.startsWith(arguments[1], true) }
            }

            else -> {
                emptyList()
            }
        }

    //TODO auslagern?
    private fun Array<String>.extractPerkAndPerkPropertyAndExecute(
        commandSender: CommandSender, action: (Perk, PerkProperty) -> Unit
    ) {
        if (this.size == 2) {
            val perk = perkRegistry.findPerkByIdentifier(this[0])
            if (perk == null) {
                commandSender.sendPerkNotFound(this[0])
                return
            }

            val perkProperty = PerkProperty.findPerkPropertyByConfigurationKey(this[1])
            if (perkProperty == null) {
                commandSender.sendPerkPropertyNotFound(this[1])
                return
            }

            action(perk, perkProperty)
        }
    }

    private fun CommandSender.sendPerkPropertyNotFound(input: String) =
        sendPluginMessage("perk_property_not_found", "input" to input)
}
