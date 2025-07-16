package art.flieda.advancedperks.event

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.event.perk.PerkEnableEvent
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import io.kotest.matchers.shouldBe
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.koin.core.component.get

class PerkEnableEventTest : AbstractMockBukkitTest() {

    @Test
    fun `perk enable event gets called with the correct player and perk`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!


        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onPerkEnable(event: PerkEnableEvent) {
                event.getPlayer() shouldBe player
                event.getPerk() shouldBe perk
            }
        }, plugin)


        get<PerkStateController>().forceEnablePerk(player, perk)
    }

    @Test
    fun `perk enable event will not get called when the perk is enable a second time after already being enabled`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        get<PerkStateController>().forceEnablePerk(player, perk)


        server.pluginManager.registerEvents(object : Listener {
            @EventHandler
            fun onPerkEnable(ignored: PerkEnableEvent) {
                fail { "Event was called after enabling a already enabled perk." }
            }
        }, plugin)


        get<PerkStateController>().forceEnablePerk(player, perk)
    }

}
