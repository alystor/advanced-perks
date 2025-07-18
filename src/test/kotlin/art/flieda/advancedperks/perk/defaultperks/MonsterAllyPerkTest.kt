package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import org.junit.jupiter.api.Test

class MonsterAllyPerkTest : AbstractMockBukkitTest() {

    @Test
    fun `monsters will not target the player when he has the perk activated`() {
        """
            Unable to properly test this perk.
            MockBukkit doesnt trigger the EntityTargetLivingEntity event when doing entity.setTarget().
        """
    }

}
