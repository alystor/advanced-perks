package art.flieda.advancedperks.perk.defaultperks

import org.junit.jupiter.api.Test

class FireFlyPerkTest : AbstractEffectPerkTest() {

    @Test
    fun `give the player the effect on enable and remove it on disable`() {
        testPotionEffectOnEnableAndDisable("firefly")
    }

}
