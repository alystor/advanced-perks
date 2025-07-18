package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import org.junit.jupiter.api.Test

class InsomniaPerkTest : AbstractMockBukkitTest() {

    @Test
    fun `remove a phantom that targets the player when having insomnia activated`() {
        """
            Unable to properly test this perk.
            MockBukkit doesnt trigger the EntityTargetLivingEntity event when doing entity.setTarget().
        """
    }

}
