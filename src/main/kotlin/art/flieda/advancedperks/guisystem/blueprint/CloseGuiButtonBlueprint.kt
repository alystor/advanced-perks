package art.flieda.advancedperks.guisystem.blueprint

import art.flieda.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprint
import art.flieda.advancedperks.guisystem.blueprint.representation.defaultMissingRepresentation

data class CloseGuiButtonBlueprint(
    val enabled: Boolean,
    val slot: Int = 0,
    val representation: GuiRepresentationBlueprint = defaultMissingRepresentation(),
)
