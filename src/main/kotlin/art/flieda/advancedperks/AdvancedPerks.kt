package art.flieda.advancedperks

import org.bukkit.plugin.Plugin
import org.koin.core.context.GlobalContext


object AdvancedPerks {

    const val PLUGIN_COMMAND_ALIAS = "perks"

    val logger = GlobalContext.get().get<Plugin>().logger

}
