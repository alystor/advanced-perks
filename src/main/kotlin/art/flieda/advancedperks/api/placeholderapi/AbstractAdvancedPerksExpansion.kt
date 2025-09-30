package art.flieda.advancedperks.api.placeholderapi

import me.clip.placeholderapi.expansion.PlaceholderExpansion
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

abstract class AbstractAdvancedPerksExpansion : PlaceholderExpansion(), KoinComponent {

    val plugin: Plugin = get<Plugin>()

    init {
        register()
    }

    override fun getAuthor(): String = plugin.description.authors.joinToString(", ")

    override fun getVersion(): String = plugin.description.version

}
