package art.flieda.advancedperks.compatitbility.bukkit

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ChangeWorldPerkCompatibilityHandlerTest : AbstractMockBukkitTest() {

    @Test
    fun `disable all perks that are disallowed when a player changes to another world`() {
        val player = server.addPlayer()
        val otherWorld = server.addSimpleWorld("other_world")
        val allowedPerk = player.forceEnablePerk("bat")
        val disallowedPerk = player.forceEnablePerk("farmer").apply {
            setDisallowedWorlds(listOf(otherWorld.name))
        }

        player.getOrCreatePerkData().hasPerkEnabled(allowedPerk) shouldBe true
        player.getOrCreatePerkData().hasPerkEnabled(disallowedPerk) shouldBe true


        player.teleport(otherWorld.spawnLocation)


        player.getOrCreatePerkData().hasPerkEnabled(allowedPerk) shouldBe true
        player.getOrCreatePerkData().hasPerkEnabled(disallowedPerk) shouldBe false
    }

}