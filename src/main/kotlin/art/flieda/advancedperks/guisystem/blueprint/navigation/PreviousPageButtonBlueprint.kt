package art.flieda.advancedperks.guisystem.blueprint.navigation

import art.flieda.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprint

data class PreviousPageButtonBlueprint(
    val showAlways: Boolean,
    val slot: Int,
    val representation: GuiRepresentationBlueprint,
)
