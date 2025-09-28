package art.flieda.advancedperks.guisystem

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class GuiSystemEventHandler : KoinComponent, Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, get<Plugin>())
    }

    //TODO add tests
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val guiWindow = event.clickedInventory?.holder as? GuiWindow<*> ?: return

        event.isCancelled = true

        val clickedItemId = event.currentItem?.getGuiElementId()

        val guiElement = guiWindow.elements[clickedItemId] ?: return
        guiElement.handleInventoryClick(event)
    }

}
