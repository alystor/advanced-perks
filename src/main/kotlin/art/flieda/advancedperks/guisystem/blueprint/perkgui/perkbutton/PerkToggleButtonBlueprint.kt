package art.flieda.advancedperks.guisystem.blueprint.perkgui.perkbutton

import art.flieda.advancedperks.guisystem.blueprint.representation.GuiRepresentationBlueprint
import art.flieda.advancedperks.guisystem.blueprint.representation.defaultMissingRepresentation

data class PerkToggleButtonBlueprint(
    val enabled: Boolean,
    val inventorySlots: List<Int> = emptyList(),
    val activeRepresentation: GuiRepresentationBlueprint = defaultMissingRepresentation(),
    val inactiveRepresentation: GuiRepresentationBlueprint = defaultMissingRepresentation(),
)
