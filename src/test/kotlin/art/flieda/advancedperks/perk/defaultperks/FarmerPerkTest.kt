package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import org.bukkit.Material
import org.bukkit.block.data.Ageable
import org.bukkit.entity.Player
import org.junit.jupiter.api.Test

class FarmerPerkTest : AbstractMockBukkitTest() {

    @Test
    fun `crop should automatically replant when broken at maximum age`() {
        val player = server.addPlayer()
        player.location.block.type = Material.WATER
        val perk = player.forceEnablePerk("farmer")
        val block = plantWheatAtFixedLocation(player, 7)


        player.getSimulator().simulateBlockBreak(block)
        server.scheduler.performTicks(1)


        player.getOrCreatePerkData().hasPerkEnabled(perk) shouldBe true
        block.type shouldBe Material.WHEAT
    }

    @Test
    fun `crop should not automatically replant when broken at an age below maximum`() {
        val player = server.addPlayer()
        player.location.block.type = Material.WATER
        val perk = player.forceEnablePerk("farmer")
        val block = plantWheatAtFixedLocation(player, 1)


        player.getSimulator().simulateBlockBreak(block)
        server.scheduler.performTicks(1)


        player.getOrCreatePerkData().hasPerkEnabled(perk) shouldBe true
        block.type shouldBe Material.AIR
    }

    private fun plantWheatAtFixedLocation(player: Player, cropAge: Int) =
        player.world.getBlockAt(1, 1, 1).also { block ->
            block.type = Material.WHEAT
            (block.blockData as? Ageable)?.let { it.age = cropAge; block.blockData = it }
        }
}
