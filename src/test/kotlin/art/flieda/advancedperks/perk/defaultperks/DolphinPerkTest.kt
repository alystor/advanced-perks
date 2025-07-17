package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.bukkit.Material
import org.junit.jupiter.api.Test

class DolphinPerkTest : AbstractMockBukkitTest() {

    @Test
    fun `player should receive a velocity boost when hes sneaking and touching water at the same time`() {
        val player = server.addPlayer()
        player.location.block.type = Material.WATER
        val perk = player.forceEnablePerk("dolphin")


        player.getSimulator().simulateSneak(true)


        player.getOrCreatePerkData().hasPerkEnabled(perk) shouldBe true
        player.velocity.isZero shouldNotBe true
    }

    @Test
    fun `player should not receive a velocity boost when hes sneaking but not touching water`() {
        val player = server.addPlayer()
        val perk = player.forceEnablePerk("dolphin")


        player.getSimulator().simulateSneak(true)


        player.getOrCreatePerkData().hasPerkEnabled(perk) shouldBe true
        player.velocity.isZero shouldNotBe false
    }

}
