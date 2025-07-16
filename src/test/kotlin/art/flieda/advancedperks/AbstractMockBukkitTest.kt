package art.flieda.advancedperks

import org.bukkit.permissions.PermissionAttachment
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.component.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.mockbukkit.mockbukkit.MockBukkit
import org.mockbukkit.mockbukkit.ServerMock
import org.mockbukkit.mockbukkit.entity.PlayerMock
import java.util.UUID


abstract class AbstractMockBukkitTest(
    private val additionalModule: Module? = null,
) : KoinComponent {

    internal lateinit var server: ServerMock
    internal lateinit var plugin: BukkitBootstrap

    private val permissionAttachmentCache = mutableMapOf<UUID, PermissionAttachment>()

    @BeforeEach
    fun setUp() {
        stopKoin()
        server = MockBukkit.mock()
        plugin = MockBukkit.load(BukkitBootstrap::class.java)
        additionalModule?.let { loadKoinModules(it) }
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
        permissionAttachmentCache.clear()
    }

    fun PlayerMock.addPermission(permission: String): PlayerMock = this.apply {
        permissionAttachmentCache.getOrPut(uniqueId) { addAttachment(plugin) }.setPermission(permission, true)
    }

}
