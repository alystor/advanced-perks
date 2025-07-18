package art.flieda.advancedperks

import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.testutil.mockbukkit.ExtendedPlayerSimulation
import org.bukkit.damage.DamageSource
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.permissions.PermissionAttachment
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
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
    private val playerSimulationCache = mutableMapOf<Player, ExtendedPlayerSimulation>()

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
        playerSimulationCache.clear()
    }

    internal fun PlayerMock.forceDisablePerk(identifier: String) =
        get<PerkRegistry>().findPerkByIdentifier(identifier)!!
            .also { get<PerkStateController>().forceDisablePerk(this, it) }

    internal fun PlayerMock.forceEnablePerk(identifier: String) =
        get<PerkRegistry>().findPerkByIdentifier(identifier)!!
            .also { get<PerkStateController>().forceEnablePerk(this, it) }

    internal fun PlayerMock.withPermission(permission: String): PlayerMock = this.apply {
        permissionAttachmentCache.getOrPut(uniqueId) { addAttachment(plugin) }.setPermission(permission, true)
    }

    internal fun PlayerMock.getSimulator(): ExtendedPlayerSimulation = playerSimulationCache.getOrPut(this) {
        ExtendedPlayerSimulation(this)
    }

    internal fun PlayerMock.applyFallDamage(damage: Double) = this.apply {
        val event = EntityDamageEvent(
            this,
            EntityDamageEvent.DamageCause.FALL,
            DamageSource.builder(DamageType.FALL).build(),
            damage
        )
        server.pluginManager.callEvent(event)
        if (event.isCancelled.not()) {
            damage(event.damage)
        }
    }

}
