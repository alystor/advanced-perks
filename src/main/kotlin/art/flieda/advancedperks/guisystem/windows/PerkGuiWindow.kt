package art.flieda.advancedperks.guisystem.windows

import art.flieda.advancedperks.guisystem.GuiWindow
import art.flieda.advancedperks.guisystem.blueprint.perkgui.PerkGuiBlueprint
import art.flieda.advancedperks.guisystem.blueprint.perkgui.perkbutton.PerkButtonBlueprint
import art.flieda.advancedperks.guisystem.elements.CloseGuiElement
import art.flieda.advancedperks.guisystem.elements.NextPageElement
import art.flieda.advancedperks.guisystem.elements.PreviousPageElement
import art.flieda.advancedperks.guisystem.elements.perkgui.DisableAllPerksElement
import art.flieda.advancedperks.guisystem.elements.perkgui.PerkElement
import art.flieda.advancedperks.guisystem.elements.perkgui.PerkToggleElement
import art.flieda.advancedperks.perk.registry.PerkRegistry
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PerkGuiWindow(
    owner: Player,
    blueprint: PerkGuiBlueprint,
) : GuiWindow<PerkGuiBlueprint>(
    rows = blueprint.rows,
    title = blueprint.title,
    data = blueprint,
    backgroundBlueprint = blueprint.background,
    owner = owner,
), KoinComponent {

    override fun populateGuiWindow(data: PerkGuiBlueprint, page: Int) {
        data.utilityButton.closeGuiButton.let { blueprint ->
            if (blueprint.enabled) {
                addGuiElement(CloseGuiElement(this, blueprint), blueprint.slot)
            }
        }

        data.utilityButton.disableAllPerksButton.let { blueprint ->
            if (blueprint.enabled) {
                addGuiElement(DisableAllPerksElement(this, blueprint), blueprint.slot)
            }
        }

        data.navigationButton.nextPageButton.let { blueprint ->
            if (blueprint.showAlways || hasNextPage(page)) {
                addGuiElement(NextPageElement(this, blueprint), blueprint.slot)
            }
        }


        data.navigationButton.previousPageButton.let { blueprint ->
            if (blueprint.showAlways || page > 0) {
                addGuiElement(PreviousPageElement(this, blueprint), blueprint.slot)
            }
        }

        addPerkButtons(data.perkButton, page)
    }

    private fun addPerkButtons(data: PerkButtonBlueprint, page: Int) {
        val perkRegistry = get<PerkRegistry>()

        val perksPerPage = data.inventorySlots.size
        val perkStartIndex = perksPerPage.times(page).coerceAtMost(perkRegistry.perkCache.size)
        val perkEndIndex = perkStartIndex.plus(perksPerPage).coerceAtMost(perkRegistry.perkCache.size)
        val perksForPage = perkRegistry.perkCache.values.toList().subList(perkStartIndex, perkEndIndex)
        perksForPage.forEachIndexed { index, perk ->
            addGuiElement(PerkElement(this, perk), data.inventorySlots[index])

            if (data.toggleButton.enabled) {
                addGuiElement(PerkToggleElement(this, data.toggleButton, perk), data.toggleButton.inventorySlots[index])
            }
        }
    }

    override fun hasNextPage(currentPage: Int): Boolean {
        val blueprint = data ?: return false
        val perkRegistry = get<PerkRegistry>()

        val perksPerPage = blueprint.perkButton.inventorySlots.size
        return perkRegistry.perkCache.size > perksPerPage.times(currentPage).plus(perksPerPage)
    }

}
