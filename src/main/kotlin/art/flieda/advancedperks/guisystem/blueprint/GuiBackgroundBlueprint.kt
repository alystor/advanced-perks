package art.flieda.advancedperks.guisystem.blueprint

import art.flieda.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprint
import art.flieda.advancedperks.guisystem.blueprint.representation.defaultMissingRepresentation

data class GuiBackgroundBlueprint(
    val enabled: Boolean,
    val representation: GuiRepresentationBlueprint = defaultMissingRepresentation(),
)
