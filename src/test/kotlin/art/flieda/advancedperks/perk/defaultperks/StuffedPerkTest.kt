package art.flieda.advancedperks.perk.defaultperks

import org.junit.jupiter.api.Test

//TODO add tests for all of the effect desync stuff
class StuffedPerkTest : AbstractEffectPerkTest() {

    @Test
    fun `give the player the effect on enable and remove it on disable`() {
        testPotionEffectOnEnableAndDisable("stuffed")
    }

}
