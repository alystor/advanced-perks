package art.flieda.advancedperks.guisystem.elements

import art.flieda.advancedperks.guisystem.GuiElement
import art.flieda.advancedperks.guisystem.GuiWindow
import art.flieda.advancedperks.guisystem.blueprint.CloseGuiButtonBlueprint
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class CloseGuiElement(
    parentWindow: GuiWindow<*>,
    blueprint: CloseGuiButtonBlueprint
) : GuiElement(parentWindow) {

    private val icon = blueprint.representation.toGuiIcon()

    override fun getGuiIcon(): ItemStack = icon

    override fun handleInventoryClick(event: InventoryClickEvent) = event.whoClicked.closeInventory()

}
