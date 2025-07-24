package art.flieda.advancedperks.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
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

fun <T> runTaskAsync(action: suspend () -> T): Deferred<T> = pluginCoroutineScope.async {
    action()
}

fun <T> runTaskSync(action: () -> T): T {
    return if (Bukkit.isPrimaryThread()) {
        action()
    } else {
        Bukkit.getScheduler().callSyncMethod(plugin, action).get()
    }
}