package art.flieda.advancedperks.perk

import art.flieda.advancedperks.AbstractTest
import art.flieda.advancedperks.testutil.mockgeneration.PerkMockGeneration
import art.flieda.advancedperks.testutil.testdatageneration.PerkPropertiesTestDataGeneration
import io.mockk.mockk
import io.mockk.verify
import org.bukkit.entity.Player
import org.junit.jupiter.api.Test

class AbstractPerkTest : AbstractTest() {

    @Test
    fun `runWhenPerkIsEnabled only executes action block when perk is considered as enabled`() {
        val playerMock = mockk<Player>()
        val enabledPerk =
            PerkMockGeneration.aMockedAbstractPerk(PerkPropertiesTestDataGeneration.aPerkProperties(enabled = true))
        val disabledPerk =
            PerkMockGeneration.aMockedAbstractPerk(PerkPropertiesTestDataGeneration.aPerkProperties(enabled = false))
        val actionMock = mockk<(Player) -> Unit>(relaxed = true)


        enabledPerk.runWhenPerkIsEnabled(playerMock, actionMock)
        disabledPerk.runWhenPerkIsEnabled(playerMock, actionMock)

        verify(exactly = 1) { actionMock(playerMock) }
        verify(exactly = 1) { enabledPerk.runWhenPerkIsEnabled(playerMock, actionMock) }
    }

}
