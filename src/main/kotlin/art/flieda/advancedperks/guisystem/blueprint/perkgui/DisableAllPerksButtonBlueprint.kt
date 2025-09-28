package art.flieda.advancedperks.guisystem.blueprint.perkgui

import art.flieda.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprint
import art.flieda.advancedperks.guisystem.blueprint.representation.defaultMissingRepresentation

data class DisableAllPerksButtonBlueprint(
    val enabled: Boolean,
    val slot: Int = 0,
    val representation: GuiRepresentationBlueprint = defaultMissingRepresentation(),
)
