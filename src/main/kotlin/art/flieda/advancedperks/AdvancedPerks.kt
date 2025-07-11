package art.flieda.advancedperks

import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

const val PLUGIN_COMMAND_ALIAS = "perk"

class AdvancedPerks : KoinComponent {

    companion object : KoinComponent {
        val logger = get<Plugin>().logger
    }

}
