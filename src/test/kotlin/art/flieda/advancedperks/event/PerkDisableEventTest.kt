package art.flieda.advancedperks.event

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.event.perk.PerkDisableEvent
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import io.kotest.matchers.shouldBe
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.koin.core.component.get

class PerkDisableEventTest : AbstractMockBukkitTest() {

    @Test
    fun `perk disable event gets called with the correct player and perk`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!


        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onPerkDisable(event: PerkDisableEvent) {
                event.getPlayer() shouldBe player
                event.getPerk() shouldBe perk
            }
        }, plugin)


        get<PerkStateController>().forceEnablePerk(player, perk)
        get<PerkStateController>().forceDisablePerk(player, perk)
    }

    @Test
    fun `perk disable event will not get called when the perk is disabled a second time after already being disabled`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        get<PerkStateController>().forceEnablePerk(player, perk)
        get<PerkStateController>().forceDisablePerk(player, perk)


        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onPerkDisable(ignored: PerkDisableEvent) {
                fail { "Event was called after disabling a already disabled perk." }
            }
        }, plugin)


        get<PerkStateController>().forceDisablePerk(player, perk)
    }

}
