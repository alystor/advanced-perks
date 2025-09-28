package art.flieda.advancedperks.guisystem.blueprint.perkgui

import art.flieda.advancedperks.guisystem.blueprint.GuiBackgroundBlueprint
import art.flieda.advancedperks.guisystem.blueprint.navigation.NavigationButtonBlueprint
import art.flieda.advancedperks.guisystem.blueprint.perkgui.perkbutton.PerkButtonBlueprint

private const val MAX_ALLOWED_ROWS = 6

data class PerkGuiBlueprint(
    val title: String,
    val rows: Int,
    val background: GuiBackgroundBlueprint,
    val perkButton: PerkButtonBlueprint,
    val navigationButton: NavigationButtonBlueprint,
    val utilityButton: UtilityButtonBlueprint,
) {

    //TODO add test
    init {
        require(rows in 1..MAX_ALLOWED_ROWS) { "rows must be between 1 and 6" }
    }

}
