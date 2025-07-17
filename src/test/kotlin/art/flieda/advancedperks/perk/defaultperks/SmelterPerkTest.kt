package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import org.junit.jupiter.api.Test

class SmelterPerkTest : AbstractMockBukkitTest() {

    @Test
    fun `drops the proper item when supported block is broken using this perk`() {
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
