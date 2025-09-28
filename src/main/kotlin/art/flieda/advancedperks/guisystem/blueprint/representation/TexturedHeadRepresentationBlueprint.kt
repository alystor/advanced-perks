package art.flieda.advancedperks.guisystem.blueprint.representation

import art.flieda.advancedperks.utilities.StringProcessingExtensions.applyReplacements
import art.flieda.advancedperks.utilities.itembuilder.TexturedSkullBuilder
import org.bukkit.inventory.ItemStack

data class TexturedHeadRepresentationBlueprint(
    val title: String,
    val textureValue: String,
    val amount: Int,
) : GuiRepresentationBlueprint {

    override fun toGuiIcon(): ItemStack = TexturedSkullBuilder()
        .setTextureValue(textureValue)
        .setDisplayName(title.applyReplacements())
        .setAmount(amount)
        .build()

}
