package art.flieda.advancedperks.guisystem.elements

import art.flieda.advancedperks.guisystem.GuiElement
import art.flieda.advancedperks.guisystem.GuiWindow
import art.flieda.advancedperks.guisystem.blueprint.navigation.NextPageButtonBlueprint
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class NextPageElement(
    parentWindow: GuiWindow<*>,
    blueprint: NextPageButtonBlueprint,
) : GuiElement(parentWindow) {

    private val icon = blueprint.representation.toGuiIcon()

    override fun getGuiIcon(): ItemStack = icon

    override fun handleInventoryClick(event: InventoryClickEvent) = parentWindow.nextPage()
}
