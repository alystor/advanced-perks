package art.flieda.advancedperks.guisystem.elements.perkgui

import art.flieda.advancedperks.guisystem.GuiElement
import art.flieda.advancedperks.guisystem.GuiWindow
import art.flieda.advancedperks.guisystem.Refreshable
import art.flieda.advancedperks.guisystem.blueprint.perkgui.perkbutton.PerkToggleButtonBlueprint
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PerkToggleElement(
    parentWindow: GuiWindow<*>,
    blueprint: PerkToggleButtonBlueprint,
    private val perk: Perk,
) : GuiElement(parentWindow), KoinComponent, Refreshable {

    private val activeIcon = blueprint.activeRepresentation.toGuiIcon()

    private val inactiveIcon = blueprint.inactiveRepresentation.toGuiIcon()

    private val perkStateController by lazy { get<PerkStateController>() }

    override fun getGuiIcon(): ItemStack {
        val player = parentWindow.owner.get() ?: return inactiveIcon

        return if (player.getOrCreatePerkData().hasPerkEnabled(perk)) {
            activeIcon
        } else {
            inactiveIcon
        }
    }

    override fun handleInventoryClick(event: InventoryClickEvent) {
        parentWindow.owner.get()?.let { player ->
            perkStateController.togglePerk(player, perk)
            parentWindow.refreshGuiWindow()
        }
    }

    override fun refreshGuiElement() {
        refreshIcon()
    }

}
