package art.flieda.advancedperks.guisystem.blueprint.navigation

import art.flieda.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprint

data class NextPageButtonBlueprint(
    val showAlways: Boolean,
    val slot: Int,
    val representation: GuiRepresentationBlueprint,
)
