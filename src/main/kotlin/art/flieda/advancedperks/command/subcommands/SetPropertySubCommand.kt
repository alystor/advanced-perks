package art.flieda.advancedperks.command.subcommands

import art.flieda.advancedperks.command.AbstractPerkSubCommand
import art.flieda.advancedperks.configuration.orDefault
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.properties.PerkProperty
import org.bukkit.command.CommandSender

const val SET_PROPERTY_SUB_COMMAND_PERMISSION = "advanced_perks.command.admin.set_property"

/* /perks set_property <perk> <property> <value> */
class SetPropertySubCommand : AbstractPerkSubCommand(
    name = "set_property",
    requiredPermission = listOf(SET_PROPERTY_SUB_COMMAND_PERMISSION)
) {
    override fun handleCommand(
        commandSender: CommandSender,
        arguments: Array<String>
    ) = if (arguments.size >= 3) {
        arguments.extractPerkAndPerkPropertyAndExecute(commandSender) { perk, property ->
            val result = perk.setProperty(property, arguments.drop(2).joinToString(separator = " "))
            commandSender.sendPluginMessage(
                "result.${result.name.lowercase()}",
                "perk" to perk.getIdentifier(),
                "property" to property.configurationKey,
                "example" to property.exampleValue,
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
                PerkProperty.entries.filter { it.modifiable }
                    .map { it.configurationKey }
                    .filter { it.startsWith(arguments[0], true) }
            }

            3 -> {
                val exampleValue = PerkProperty.findPerkPropertyByConfigurationKey(arguments[1])?.exampleValue
                listOf(exampleValue.orDefault("no valid property selected"))
            }

            else -> {
                emptyList()
            }
        }

    private fun Array<String>.extractPerkAndPerkPropertyAndExecute(
        commandSender: CommandSender, action: (Perk, PerkProperty) -> Unit
    ) {
        if (this.size >= 2) {
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
