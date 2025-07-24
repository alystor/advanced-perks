package art.flieda.advancedperks.lifecycle

import art.flieda.advancedperks.perkdata.PerkDataController

class LifeCycleController(
    private val perkDataController: PerkDataController
) {

    fun handlePluginOnEnable() {
        perkDataController.handlePluginOnEnable()
    }

    fun handlePluginOnDisable() {
        perkDataController.handlePluginOnDisable()
    }

}