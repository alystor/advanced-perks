package art.flieda.advancedperks.guisystem.blueprint.representation

import art.flieda.advancedperks.utilities.StringProcessingExtensions.applyReplacements
import art.flieda.advancedperks.utilities.itembuilder.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

data class ItemStackRepresentationBlueprint(
    val title: String,
    val material: Material,
    val amount: Int,
) : GuiRepresentationBlueprint {

    override fun toGuiIcon(): ItemStack = ItemStackBuilder(material)
        .setDisplayName(title.applyReplacements())
        .setAmount(amount)
        .build()

}
