package art.flieda.advancedperks.guisystem.elements.perkgui

import art.flieda.advancedperks.guisystem.GuiElement
import art.flieda.advancedperks.guisystem.GuiWindow
import art.flieda.advancedperks.guisystem.blueprint.perkgui.DisableAllPerksButtonBlueprint
import art.flieda.advancedperks.perk.state.PerkStateController
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DisableAllPerksElement(
    parentWindow: GuiWindow<*>,
    blueprint: DisableAllPerksButtonBlueprint,
) : GuiElement(parentWindow), KoinComponent {

    private val perkStateController = get<PerkStateController>()

    private val icon: ItemStack = blueprint.representation.toGuiIcon()

    override fun getGuiIcon(): ItemStack = icon

    override fun handleInventoryClick(event: InventoryClickEvent) {
        parentWindow.owner.get()?.let { player ->
            perkStateController.forceDisableAllPerks(player)
            parentWindow.refreshGuiWindow()
        }
    }

}
