package art.flieda.advancedperks.core.configuration

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object ConfigurationModule {

    val module = module {
        singleOf(::MessageConfiguration)
        singleOf(::SettingConfiguration)
    }

}