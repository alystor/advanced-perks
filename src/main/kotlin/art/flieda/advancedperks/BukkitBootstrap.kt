package art.flieda.advancedperks

import art.flieda.advancedperks.api.AdvancedPerksApi
import art.flieda.advancedperks.command.PerkCommandModule
import art.flieda.advancedperks.compatibility.CompatibilityModule
import art.flieda.advancedperks.configuration.ConfigurationModule
import art.flieda.advancedperks.utilities.ConcurrencyUtilities
import art.flieda.advancedperks.economy.adapters.EconomyAdapterModule
import art.flieda.advancedperks.lifecycle.LifeCycleController
import art.flieda.advancedperks.perk.PerkModule
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.io.File

open class BukkitBootstrap : JavaPlugin(), KoinComponent {

    val module = module {
        single<Plugin> { this@BukkitBootstrap }
        single<File>(named("pluginDirectory")) { this@BukkitBootstrap.dataFolder }
    }

    override fun onEnable() {
        startKoin {
            modules(
                module,
                MainPluginModule.module,
                PerkModule.module,
                PerkCommandModule.module,
                ConfigurationModule.module,
                ConfigurationModule.objectMapperModule,
                EconomyAdapterModule.module,
                CompatibilityModule.module,
                ConcurrencyUtilities.module,
                AdvancedPerksApi.module,
            )
        }

        get<LifeCycleController>().handlePluginOnEnable()
    }

    override fun onDisable() {
        get<LifeCycleController>().handlePluginOnDisable()
    }

}
