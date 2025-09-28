package art.flieda.advancedperks.utilities

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.koin.core.context.GlobalContext
import org.koin.dsl.module

object ConcurrencyUtilities {
    val module = module {
        single { pluginCoroutineScope }
    }
}

private val pluginCoroutineScope = CoroutineScope(Dispatchers.IO)
private val plugin = GlobalContext.get().get<Plugin>()

fun runTaskAsync(action: suspend () -> Unit) = pluginCoroutineScope.async { action() }

fun runTaskSync(action: () -> Unit) {
    if (Bukkit.isPrimaryThread()) {
        action()
    } else {
        Bukkit.getScheduler().runTask(plugin, action)
    }
}

fun runTaskSyncLater(tickDelay: Long, action: () -> Unit) =
    Bukkit.getScheduler().runTaskLater(plugin, action, tickDelay)
