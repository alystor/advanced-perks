package art.flieda.advancedperks.guisystem

import art.flieda.advancedperks.guisystem.blueprint.perkgui.PerkGuiBlueprint
import art.flieda.advancedperks.guisystem.windows.PerkGuiWindow
import com.google.gson.Gson
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class GuiSystemService : KoinComponent {

    val defaultPerkGuiBlueprint: PerkGuiBlueprint = run {
        val blueprintUrl = this::class.java.classLoader.getResource("default_gui.json")

        checkNotNull(blueprintUrl) { "default_gui.json not found in classpath" }

        blueprintUrl.openStream().use { inputStream ->
            get<Gson>().fromJson<PerkGuiBlueprint>(inputStream.reader(), PerkGuiBlueprint::class.java)
        }
    }

    //TODO add a way for custom guis

    fun openPerkGuiForPlayer(player: Player) {
        val perkGui = PerkGuiWindow(
            owner = player,
            blueprint = defaultPerkGuiBlueprint,
        )

        player.openInventory(perkGui.inventory)
    }

    companion object : KoinComponent {
        val guiElementIdKey = NamespacedKey(get<Plugin>(), "gui_element_id")
    }

}
