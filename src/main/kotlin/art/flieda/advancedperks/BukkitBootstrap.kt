package art.flieda.advancedperks

import art.flieda.advancedperks.command.PerkCommandModule
import art.flieda.advancedperks.core.configuration.ConfigurationModule
import art.flieda.advancedperks.perk.PerkModule
import art.flieda.advancedperks.perk.state.data.PerkPlayerDataController
import art.flieda.advancedperks.perk.state.data.PerkPlayerDataRepository
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.yaml.snakeyaml.Yaml
import java.io.File

class BukkitBootstrap : JavaPlugin(), KoinComponent {

    override fun onEnable() {
        startKoin {
            modules(
                module,
                PerkModule.module,
                PerkCommandModule.module,
                ConfigurationModule.module
            )
        }
        AdvancedPerks()
    }

    val module = module {
        single<Plugin> { this@BukkitBootstrap }
        singleOf(::PerkRegistry) { createdAtStart() }
        single<Yaml> { Yaml() }
        single<File>(named("pluginDirectory")) { this@BukkitBootstrap.dataFolder }
        singleOf(::PerkPlayerDataRepository)
        singleOf(::PerkPlayerDataController)
        singleOf(::PerkStateController)
    }

}
