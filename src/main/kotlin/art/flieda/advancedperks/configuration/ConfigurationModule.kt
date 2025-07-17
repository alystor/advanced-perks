package art.flieda.advancedperks.configuration

import art.flieda.advancedperks.persistence.DatasourceConfiguration
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

object ConfigurationModule {

    val module = module {
        single { MessageConfiguration() }
        singleOf(::SettingConfiguration)
        singleOf(::DatasourceConfiguration)
    }

    val objectMapperModule = module {
        single {
            ObjectMapper().apply {
                //TODO check if KotlinModule is already available on spigot outside of tests
                registerModule(KotlinModule.Builder().build())
                configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true)
                propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            }
        }
    }

}
