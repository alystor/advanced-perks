package art.flieda.advancedperks.core.configuration

import art.flieda.advancedperks.persistence.DatasourceConfiguration
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object ConfigurationModule {

    val module = module {
        single { MessageConfiguration() }
        singleOf(::SettingConfiguration)
        singleOf(::DatasourceConfiguration)
    }

}
