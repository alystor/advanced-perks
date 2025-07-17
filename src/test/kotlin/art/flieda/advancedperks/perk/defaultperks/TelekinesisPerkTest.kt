package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import org.junit.jupiter.api.Test

class TelekinesisPerkTest : AbstractMockBukkitTest() {

    @Test
    fun `directly gets content of broken blocks into the player inventory when perk is active`() {
        """
            The implementation of the MockBukkit PlayerMock sadly doesn't implement the
            BLockDropItemEvent.
            As such this perk is not properly testable using MockBukkit.
            
		    if (!event.isCancelled())
		    {
		    	block.setType(Material.AIR);
		    	// todo: BlockDropItemEvent when BlockMock#getDrops is implemented.
		    }
        """

    }

}
