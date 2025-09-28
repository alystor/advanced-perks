package art.flieda.advancedperks.configuration

import art.flieda.advancedperks.utilities.StringProcessingExtensions.applyReplacements
import art.flieda.advancedperks.utilities.StringProcessingExtensions.replaceBukkitColorCodes
import art.flieda.advancedperks.utilities.StringProcessingExtensions.replaceColorPlaceholders
import art.flieda.advancedperks.utilities.StringProcessingExtensions.replaceHexColorCodes
import net.md_5.bungee.api.ChatColor

//TODO check if tests need to be moved
class MessageConfiguration(classPathUrl: String = "message.yml") : Configuration(classPathUrl) {

    fun getPrefix(): String {
        val prefix = getMessageStringFromKey("prefix")

        return prefix
            .replaceBukkitColorCodes()
            .replaceColorPlaceholders()
            .replaceHexColorCodes()
    }

    fun getMessageList(configKey: String, vararg placeHolders: Pair<String, Any>): List<String> {
        val messageList = getMessageStringListFromKey(configKey)

        return messageList.map { message ->
            message.applyReplacements(*placeHolders)
        }
    }

    fun getMessage(configKey: String, vararg placeHolders: Pair<String, Any>): String {
        val message = getMessageStringFromKey(configKey)

        return message.applyReplacements(*placeHolders)
    }

    fun getPermissionDeniedMessage(): String = getMessage("command.permission_denied")

    private fun getMessageStringListFromKey(configKey: String): List<String> =
        getListOfAny<String>(configKey).orDefault(listOf("No message found for key: '$configKey'"))


    private fun getMessageStringFromKey(configKey: String): String =
        getAny<String>(configKey).orDefault("No message found for key: '$configKey'")

    internal fun getPrimaryColor(): String =
        ChatColor.of(getAny<String>("color.primary").orDefault("#E6D7FF")).toString()

}
