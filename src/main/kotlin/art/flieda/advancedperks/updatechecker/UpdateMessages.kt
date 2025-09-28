package art.flieda.advancedperks.updatechecker

import art.flieda.advancedperks.configuration.MessageConfiguration
import art.flieda.advancedperks.utilities.StringProcessingExtensions.applyReplacements
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private const val PLUGIN_SPIGOT_URL =
    "https://www.spigotmc.org/resources/advanced-perks-recode-gui-placeholderapi-support-citybuild-faction-etc-1-16-5-1-20-x.96171/"

object UpdateMessages : KoinComponent {

    val configuration = get<MessageConfiguration>()

    fun getUpdateAvailableMessage(currentVersion: String, latestVersion: String): List<TextComponent> =
        listOf(
            " ",
            "&7A new version of <primary>Advanced Perks &7is available!",
            "&7Click <primary>here &7to download the new update.",
            "&7Current Version: <primary>{current_version} &7< Latest Version: <primary>{latest_version}",
            " "
        ).map { message ->
            message.applyReplacements(
                "current_version" to currentVersion,
                "latest_version" to latestVersion
            )
        }.map { message ->
            TextComponent(message).apply {
                clickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, PLUGIN_SPIGOT_URL)
            }
        }

    fun getErrorMessage(errorMessage: String): List<TextComponent> =
        listOf(
            " ",
            "&cAn error occurred while checking for updates.",
            "&cError: {error_message}",
            " "
        ).map { message ->
            message.applyReplacements("error_message" to errorMessage)
        }.map { message ->
            TextComponent(message)
        }

    fun getTimeoutMessage(): List<TextComponent> =
        listOf(
            " ",
            "&cThe update check timed out.",
            "&cPlease try again later or check the plugin page manually.",
            " "
        ).map { message ->
            TextComponent(message)
        }

}
