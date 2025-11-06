package art.flieda.advancedperks.guisystem

import art.flieda.advancedperks.guisystem.blueprint.GuiBackgroundBlueprint
import art.flieda.advancedperks.guisystem.elements.GuiBackgroundElement
import art.flieda.advancedperks.utilities.StringProcessingExtensions.applyReplacements
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.lang.ref.WeakReference
import java.util.UUID

private const val INVENTORY_ROW_SIZE = 9

abstract class GuiWindow<T>(
    rows: Int,
    title: String,
    owner: Player,
    internal val data: T? = null,
    private var page: Int = 0,
    private val backgroundBlueprint: GuiBackgroundBlueprint = GuiBackgroundBlueprint(enabled = false),
) : InventoryHolder {

    val owner = WeakReference(owner)

    val elements = mutableMapOf<UUID, GuiElement>()

    private val inventory = Bukkit.createInventory(this, rows.times(INVENTORY_ROW_SIZE), title.applyReplacements())

    init {
        constructBackground()
        data?.let { populateGuiWindow(it, page) }
    }

    abstract fun populateGuiWindow(data: T, page: Int)

    abstract fun hasNextPage(currentPage: Int): Boolean

    fun addGuiElement(guiElement: GuiElement, slot: Int) {
        /* Cleanup potential overwritten gui element */
        inventory.getItem(slot)?.let { item ->
            val elementId = item.findGuiElementId() ?: return@let
            elements.remove(elementId)
            inventory.setItem(slot, null)
        }

        elements[guiElement.id] = guiElement
        val iconWithId = guiElement.getGuiIcon().clone().setGuiElementId(guiElement.id)

        inventory.setItem(slot, iconWithId)
    }

    fun refreshIcon(guiElement: GuiElement) {
        val slot = inventory.contents.indexOfFirst { itemStack ->
            itemStack.findGuiElementId() == guiElement.id
        }

        if (slot != -1) {
            inventory.setItem(slot, guiElement.getGuiIcon().clone().setGuiElementId(guiElement.id))
        }
    }

    fun nextPage() {
        if (hasNextPage(page)) {
            page++
            resetGuiWindow()
            constructBackground()
            data?.let { populateGuiWindow(it, page) }
        }
    }

    fun previousPage() {
        if (page > 0) {
            page--
            resetGuiWindow()
            constructBackground()
            data?.let { populateGuiWindow(it, page) }
        }
    }

    fun refreshGuiWindow() {
        elements.forEach { (_, element) ->
            if (element is Refreshable) {
                element.refreshGuiElement()
            }
        }
    }

    fun openGuiWindow(player: Player) = player.openInventory(inventory)

    private fun resetGuiWindow() {
        inventory.clear()
        elements.clear()
    }

    private fun constructBackground() {
        if (backgroundBlueprint.enabled) {
            val element = GuiBackgroundElement(this, backgroundBlueprint)
            for (slot in 0 until inventory.size) {
                addGuiElement(element, slot)
            }
        }
    }

    override fun getInventory(): Inventory = inventory

}

fun Inventory?.findGuiWindow(): GuiWindow<*>? =
    this?.holder as? GuiWindow<*>
