package art.flieda.advancedperks.compatitbility.plugin

import art.flieda.advancedperks.AbstractMockBukkitTest
import org.junit.jupiter.api.Test

class LuckPermsCompatibilityHandlerTest : AbstractMockBukkitTest() {

    @Test
    fun `player that loses a permission node for a active perk will have said perk deactivated`() {
        """
            Unable to test LuckPermsCompatibilityHandler as it requires the LuckPerms plugin to be installed.
            I haven't found a way to mock the LuckPerms API properly yet.
        """
    }

}
