package art.flieda.advancedperks.utilities

import art.flieda.advancedperks.configuration.MessageConfiguration
import net.md_5.bungee.api.ChatColor
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.regex.Pattern

object StringProcessingExtensions : KoinComponent {

    private val messageConfiguration = get<MessageConfiguration>()

    private val hexRegexPattern: Pattern = Pattern.compile("<(#[a-fA-F0-9]{6})>")

    private val colorPlaceholders = listOf(
        "primary" to messageConfiguration.getPrimaryColor(),
    )

    private val defaultPlaceholders = listOf(
        "prefix" to messageConfiguration.getPrefix(),
    )

    fun String.applyReplacements(vararg placeHolders: Pair<String, Any>): String = this
        .replaceColorPlaceholders()
        .replacePlaceholders(placeHolders.toList())
        .replaceHexColorCodes()
        .replaceBukkitColorCodes()


    fun String.replaceColorPlaceholders(): String {
        var message = this
        for (placeHolder in colorPlaceholders) {
            message = message.replace("<${placeHolder.first}>", placeHolder.second)
        }
        return message
    }

    fun String.replacePlaceholders(placeHolders: List<Pair<String, Any>>): String {
        var message = this
        for (placeHolder in placeHolders + defaultPlaceholders) {
            message = message.replace("{${placeHolder.first}}", placeHolder.second.toString())
        }
        return message
    }

    fun String.replaceHexColorCodes(): String {
        var message = this
        var matcher = hexRegexPattern.matcher(message)
        while (matcher.find()) {
            val hexCode = matcher.group(1)
            message = message.replace(matcher.group(), ChatColor.of(hexCode).toString())
            matcher = hexRegexPattern.matcher(message)
        }
        return message
    }

    fun String.replaceBukkitColorCodes(): String = ChatColor.translateAlternateColorCodes('&', this)

    fun String.stripColorCodes(): String = ChatColor.stripColor(this)

}
