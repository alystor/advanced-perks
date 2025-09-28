package art.flieda.advancedperks.guisystem.elements

import art.flieda.advancedperks.guisystem.GuiElement
import art.flieda.advancedperks.guisystem.GuiWindow
import art.flieda.advancedperks.guisystem.blueprint.GuiBackgroundBlueprint
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class GuiBackgroundElement(
    parentWindow: GuiWindow<*>,
    backgroundBlueprint: GuiBackgroundBlueprint,
) : GuiElement(parentWindow) {

    private val icon = backgroundBlueprint.representation.toGuiIcon()

    override fun getGuiIcon(): ItemStack = icon

    @SuppressWarnings("EmptyFunctionBlock")
    override fun handleInventoryClick(ignored: InventoryClickEvent) {}

}
