package art.flieda.advancedperks.core.configuration

import net.md_5.bungee.api.ChatColor
import java.util.regex.Pattern

class MessageConfiguration(classPathUrl: String = "message.yml") : Configuration(classPathUrl) {

    private val hexRegexPattern: Pattern = Pattern.compile("<(#[a-fA-F0-9]{6})>")

    private val colorPlaceholders = listOf(
        "primary" to getPrimaryColor(),
    )

    private val defaultPlaceholders = listOf(
        "prefix" to getPrefix(),
    )

    fun getPrefix(): String {
        var prefix = getMessageStringFromKey("prefix")

        return prefix
            .replaceBukkitColorCodes()
            .replaceColorPlaceholders()
            .replaceHexColorCodes()
    }

    fun getMessage(configKey: String, vararg placeHolders: Pair<String, Any>): String {
        var message = getMessageStringFromKey(configKey)

        return message
            .replaceColorPlaceholders()
            .replacePlaceholders(placeHolders.toList())
            .replacePlaceholders(defaultPlaceholders)
            .replaceHexColorCodes()
            .replaceBukkitColorCodes()
    }

    fun getPermissionDeniedMessage(): String = getMessage("command.permission_denied")

    private fun String.replacePlaceholders(placeHolders: List<Pair<String, Any>>): String {
        var message = this
        for (placeHolder in placeHolders) {
            message = message.replace("{${placeHolder.first}}", placeHolder.second.toString())
        }
        return message
    }

    private fun String.replaceColorPlaceholders(): String {
        var message = this
        for (placeHolder in colorPlaceholders) {
            message = message.replace("<${placeHolder.first}>", placeHolder.second)
        }
        return message
    }

    private fun String.replaceHexColorCodes(): String {
        var message = this
        var matcher = hexRegexPattern.matcher(message)
        while (matcher.find()) {
            val hexCode = matcher.group(1)
            message = message.replace(matcher.group(), ChatColor.of(hexCode).toString())
            matcher = hexRegexPattern.matcher(message)
        }
        return message
    }

    private fun String.replaceBukkitColorCodes(): String = ChatColor.translateAlternateColorCodes('&', this)

    private fun getMessageStringFromKey(configKey: String): String =
        getString(configKey) ?: "No message found for key: '$configKey'"

    private fun getPrimaryColor(): String = ChatColor.of(getString("color.primary", "#E6D7FF")).toString()

}
