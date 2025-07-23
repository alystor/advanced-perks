package art.flieda.advancedperks.updatechecker

import art.flieda.advancedperks.configuration.MessageConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.net.http.HttpTimeoutException
import java.time.Duration
import java.util.UUID

const val UPDATE_CHECK_NOTIFICATION_PERMISSION = "advanced_perks.update_notification"
private const val UPDATE_CHECKER_URL = "https://api.spiget.org/v2/resources/96171/versions/latest"
private const val HTTP_STATUS_OK = 200

class UpdateCheckerController : KoinComponent, Listener {

    val objectMapper = get<ObjectMapper>()
    val plugin = get<Plugin>()
    val updateCheckerResult = checkForUpdate()
    val configuration = get<MessageConfiguration>()
    val updateMessageSendCache = mutableSetOf<UUID>()

    init {
        if ((updateCheckerResult is UpdateCheckerResult.NoUpdateAvailable).not()) {
            Bukkit.getPluginManager().registerEvents(this, plugin)
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        if (updateMessageSendCache.add(player.uniqueId).not()) {
            return
        }

        updateMessageSendCache.add(player.uniqueId)
        if (player.hasPermission(UPDATE_CHECK_NOTIFICATION_PERMISSION)) {
            when (updateCheckerResult) {
                is UpdateCheckerResult.UpdateAvailable -> {
                    UpdateMessages.getUpdateAvailableMessage(plugin.description.version, updateCheckerResult.version)
                        .forEach { message ->
                            player.spigot().sendMessage(message)
                        }
                }

                is UpdateCheckerResult.ErrorOnCheck -> {
                    UpdateMessages.getErrorMessage(updateCheckerResult.errorMessage)
                        .forEach { message ->
                            player.spigot().sendMessage(message)
                        }
                }

                is UpdateCheckerResult.TimeoutOnCheck -> {
                    UpdateMessages.getTimeoutMessage().forEach { message ->
                        player.spigot().sendMessage(message)
                    }
                }

                else -> {}
            }
        }
    }

    fun checkForUpdate(): UpdateCheckerResult {
        val request =
            HttpRequest.newBuilder().uri(URI.create(UPDATE_CHECKER_URL)).GET().timeout(Duration.ofSeconds(3)).build()

        return runCatching {
            val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
            if (response.statusCode() != HTTP_STATUS_OK) {
                return UpdateCheckerResult.ErrorOnCheck(response.body())
            }
            objectMapper.readValue(response.body(), UpdateData::class.java).run {
                if (name.toSemanticVersion() > plugin.description.version.toSemanticVersion()) {
                    UpdateCheckerResult.UpdateAvailable(name, releaseDateFormatted)
                } else {
                    UpdateCheckerResult.NoUpdateAvailable
                }
            }
        }.getOrElse {
            if (it is HttpTimeoutException) {
                UpdateCheckerResult.TimeoutOnCheck
            } else {
                UpdateCheckerResult.ErrorOnCheck(it.message ?: "Unknown error occurred during update check.")
            }
        }
    }

}
