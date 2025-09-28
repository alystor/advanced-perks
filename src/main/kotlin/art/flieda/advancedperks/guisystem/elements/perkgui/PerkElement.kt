package art.flieda.advancedperks.guisystem.elements.perkgui

import art.flieda.advancedperks.guisystem.GuiElement
import art.flieda.advancedperks.guisystem.GuiWindow
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.state.PerkStateController
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PerkElement(
    parentWindow: GuiWindow<*>,
    private val perk: Perk,
) : GuiElement(parentWindow), KoinComponent {

    private val perkStateController by lazy { get<PerkStateController>() }

    override fun getGuiIcon(): ItemStack = perk.getPerkIcon().icon

    override fun handleInventoryClick(event: InventoryClickEvent) {
        parentWindow.owner.get()?.let { player ->
            perkStateController.togglePerk(player, perk)
            parentWindow.refreshGuiWindow()
        }
    }

}
