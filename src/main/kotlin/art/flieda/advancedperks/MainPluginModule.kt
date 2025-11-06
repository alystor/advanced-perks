package art.flieda.advancedperks

import art.flieda.advancedperks.economy.PerkEconomyController
import art.flieda.advancedperks.guisystem.GuiSystemEventHandler
import art.flieda.advancedperks.guisystem.GuiSystemService
import art.flieda.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprint
import art.flieda.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprintTypeAdapter
import art.flieda.advancedperks.lifecycle.LifeCycleController
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.PerkDataController
import art.flieda.advancedperks.perkdata.repository.PerkDataRepository
import art.flieda.advancedperks.persistence.DatasourceManager
import art.flieda.advancedperks.updatechecker.UpdateCheckerController
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import org.yaml.snakeyaml.Yaml

object MainPluginModule {

    val module = module {
        singleOf(::PerkRegistry) { createdAtStart() }
        single<Yaml> { Yaml() }
        singleOf(::PerkDataRepository)
        singleOf(::PerkDataController) { createdAtStart() }
        singleOf(::PerkStateController)
        singleOf(::DatasourceManager) { createdAtStart() }
        singleOf(::PerkEconomyController)
        singleOf(::UpdateCheckerController) { createdAtStart() }
        singleOf(::LifeCycleController) { createdAtStart() }
        singleOf(::GuiSystemService) { createdAtStart() }
        singleOf(::GuiSystemEventHandler) { createdAtStart() }
        single<Gson> {
            GsonBuilder()
                .registerTypeAdapter(GuiRepresentationBlueprint::class.java, GuiRepresentationBlueprintTypeAdapter())
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        }
    }

}
