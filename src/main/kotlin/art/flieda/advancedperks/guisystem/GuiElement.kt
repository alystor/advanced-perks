package art.flieda.advancedperks.guisystem

import art.flieda.advancedperks.utilities.uuidPersistentDataType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import java.util.UUID

abstract class GuiElement(
    val parentWindow: GuiWindow<*>
) {

    val id: UUID = UUID.randomUUID()

    abstract fun getGuiIcon(): ItemStack

    abstract fun handleInventoryClick(event: InventoryClickEvent)

    fun refreshIcon() {
        parentWindow.refreshIcon(this)
    }

}

//TODO add tests
fun ItemStack.findGuiElementId(): UUID? = this.itemMeta?.persistentDataContainer?.get(
    GuiSystemService.guiElementIdKey,
    uuidPersistentDataType,
)

//TODO add tests
fun ItemStack.setGuiElementId(id: UUID): ItemStack = also {
    itemMeta = itemMeta!!.apply {
        persistentDataContainer.set(
            GuiSystemService.guiElementIdKey,
            uuidPersistentDataType,
            id,
        )
    }
}
