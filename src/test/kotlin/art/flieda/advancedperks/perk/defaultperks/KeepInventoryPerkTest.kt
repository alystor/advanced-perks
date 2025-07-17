package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.junit.jupiter.api.Test

class KeepInventoryPerkTest : AbstractMockBukkitTest() {

    @Test
    fun `player keeps the items in his inventory on death`() {
        """
            The implementation of the MockBukkit PlayerMock sadly doesn't implement the
            keepInventory flag from the PlayerDeathEvent.
            As such this perk is not properly testable using MockBukkit.
            
            if (!getWorld().getGameRuleValue(GameRule.KEEP_INVENTORY) *is missing here*)
                {
            		getInventory().clear();
            		// Should someone try to provoke a RespawnEvent, they will now find the Inventory to be empty
            	}
        """
    }

    @Test
    fun `the keepInventory flag of the PlayerDeathEvent is properly set to true`() {
        val player = server.addPlayer()
        val perk = player.forceEnablePerk("keep_inventory")

        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler(priority = EventPriority.MONITOR)
            fun onPlayerDeath(event: PlayerDeathEvent) {
                event.player.getOrCreatePerkData().hasPerkEnabled(perk) shouldBe true
                event.player shouldBe player
                event.keepInventory shouldBe true
            }
        }, plugin)

        player.damage(20.0)
    }

}
