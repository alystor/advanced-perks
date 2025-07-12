package art.flieda.advancedperks.perk

import art.flieda.advancedperks.AbstractTest
import art.flieda.advancedperks.testutil.mockgeneration.PerkMockGeneration
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
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
        val enabledAction = mockk<() -> Unit>()
        val disabledAction = mockk<() -> Unit>()
        every { enabledAction.invoke() } just Runs
        every { disabledAction.invoke() } just Runs


        enabledPerk.runWhenPerkIsEnabled(playerMock) { enabledAction.invoke() }
        disabledPerk.runWhenPerkIsEnabled(playerMock) { disabledAction.invoke() }


        verify(exactly = 1) { enabledAction.invoke() }
        verify(exactly = 0) { disabledAction.invoke() }
    }

}
