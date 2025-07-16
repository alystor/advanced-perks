package art.flieda.advancedperks.event

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.event.perk.PerkToggleEvent
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perk.state.PerkToggleStatus
import io.kotest.matchers.shouldBe
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class PerkToggleEventTest : AbstractMockBukkitTest() {

    @Test
    fun `perk toggle event gets called with the correct player and perk and status if a perk gets enabled`() {
        var eventCalled = false
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!


        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onPerkToggle(event: PerkToggleEvent) {
                event.getPlayer() shouldBe player
                event.getPerk() shouldBe perk
                event.getStatus() shouldBe PerkToggleStatus.Enabled
                eventCalled = true
            }
        }, plugin)


        get<PerkStateController>().forceEnablePerk(player, perk)
        eventCalled shouldBe true
    }

    @Test
    fun `perk toggle event gets called with the correct player and perk and status if a perk gets disabled`() {
        var eventCalled = false
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        get<PerkStateController>().forceEnablePerk(player, perk)


        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onPerkToggle(event: PerkToggleEvent) {
                event.getPlayer() shouldBe player
                event.getPerk() shouldBe perk
                event.getStatus() shouldBe PerkToggleStatus.Disabled
                eventCalled = true
            }
        }, plugin)


        get<PerkStateController>().forceDisablePerk(player, perk)
        eventCalled shouldBe true
    }

    @Test
    fun `cancelling the event should actually stop the perk from being enabled`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!


        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onPerkToggle(event: PerkToggleEvent) {
                event.isCancelled = true
            }
        }, plugin)
        val status = get<PerkStateController>().forceEnablePerk(player, perk)


        status shouldBe PerkToggleStatus.CancelledByEvent
    }

    @Test
    fun `cancelling the event should actually stop the perk from being disabled`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        get<PerkStateController>().forceEnablePerk(player, perk)


        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onPerkToggle(event: PerkToggleEvent) {
                event.isCancelled = true
            }
        }, plugin)
        val status = get<PerkStateController>().forceDisablePerk(player, perk)


        status shouldBe PerkToggleStatus.CancelledByEvent
    }

}
