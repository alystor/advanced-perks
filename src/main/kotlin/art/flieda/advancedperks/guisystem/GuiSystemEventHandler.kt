package art.flieda.advancedperks.guisystem

import art.flieda.advancedperks.event.perk.PerkToggleEvent
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
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
        val guiWindow = event.inventory.findGuiWindow() ?: return

        event.isCancelled = true

        val guiElement = guiWindow.elements[event.currentItem?.findGuiElementId()] ?: return
        guiElement.handleInventoryClick(event)
    }

    //TODO handle when perk status doesnt change
    @EventHandler(priority = EventPriority.MONITOR)
    fun onPerkToggle(event: PerkToggleEvent) {
        Bukkit.getOnlinePlayers().forEach { player ->
            val guiWindow = player.openInventory.topInventory.findGuiWindow()

            if (guiWindow != null && event.getPlayer() == guiWindow.owner.get()) {
                guiWindow.refreshGuiWindow()
            }
        }
    }

}
