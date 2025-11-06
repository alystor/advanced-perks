package art.flieda.advancedperks.compatibility.dependency

import art.flieda.advancedperks.AdvancedPerks.logger
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import net.luckperms.api.LuckPermsProvider
import net.luckperms.api.event.node.NodeRemoveEvent
import net.luckperms.api.model.user.User
import net.luckperms.api.node.NodeType
import net.luckperms.api.node.types.PermissionNode
import org.bukkit.Bukkit
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.util.function.Consumer

class LuckPermsCompatibilityHandler : KoinComponent {

    init {
        logger.info("Found LuckPerms installed on the server, the plugin started its compatability layer for LuckPerms events.")

        val api = LuckPermsProvider.get()
        api.eventBus.subscribe(
            NodeRemoveEvent::class.java, runNodeRemoveEvent()
        )
    }

    val perkStateController = get<PerkStateController>()

    private fun runNodeRemoveEvent(): Consumer<NodeRemoveEvent> = Consumer { event ->
        if (event.isUser && event.node.type == NodeType.PERMISSION) {
            val player = Bukkit.getPlayer((event.target as User).uniqueId) ?: return@Consumer
            val removedPermission =
                (event.node as PermissionNode).permission.takeIf { it.isNotBlank() } ?: return@Consumer

            player.getOrCreatePerkData().getEnabledAndLoadedPerks()
                .forEach { perk ->
                    perk.getPermission()?.let { perkPermission ->
                        if (perkPermission == removedPermission) {
                            perkStateController.forceDisablePerk(player, perk)
                        }
                    }
                }
        }

    }

}
