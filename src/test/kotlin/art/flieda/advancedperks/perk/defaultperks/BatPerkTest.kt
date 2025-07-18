package art.flieda.advancedperks.perk.defaultperks

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

//TODO add tests for all of the effect desync stuff
class BatPerkTest : AbstractEffectPerkTest() {

    @Test
    fun `give the player the effect on enable and remove it on disable`() {
        val player = server.addPlayer()


        val perk = player.forceEnablePerk("bat")
        player shouldHaveAllPotionEffectsOfPerk perk


        player.forceDisablePerk("bat")
        player.activePotionEffects.isEmpty() shouldBe true
    }

}
