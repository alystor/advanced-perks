package art.flieda.advancedperks.perk.defaultperks

import org.junit.jupiter.api.Test

class FireProofPerkTest : AbstractEffectPerkTest() {

    @Test
    fun `give the player the effect on enable and remove it on disable`() {
        testPotionEffectOnEnableAndDisable("fire_proof")
    }

}
