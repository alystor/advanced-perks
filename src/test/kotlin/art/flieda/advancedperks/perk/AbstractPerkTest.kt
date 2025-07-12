package art.flieda.advancedperks.perk

import art.flieda.advancedperks.AbstractTest
import art.flieda.advancedperks.testutil.mockgeneration.PerkMockGeneration
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.junit.jupiter.api.Test

class AbstractPerkTest : AbstractTest() {

    @Test
    fun `runWhenPerkIsEnabled only executes action block when perk is considered as enabled`() {
        val playerMock = mockk<Player>()
        val enabledPerk =
            PerkMockGeneration.aMockedAbstractPerk(enabled = true)
        val disabledPerk =
            PerkMockGeneration.aMockedAbstractPerk(enabled = false)
        val actionMock = mockk<(Player) -> Unit>(relaxed = true)


        enabledPerk.runWhenPerkIsEnabled(playerMock, actionMock)
        disabledPerk.runWhenPerkIsEnabled(playerMock, actionMock)

        verify(exactly = 1) { actionMock(playerMock) }
        verify(exactly = 1) { enabledPerk.runWhenPerkIsEnabled(playerMock, actionMock) }
    }

}
