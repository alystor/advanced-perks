package art.flieda.advancedperks.testutil.mockgeneration

import art.flieda.advancedperks.perkdata.PerkData
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import art.flieda.advancedperks.testutil.testdatageneration.PerkPlayerDataTestDataGeneration
import io.mockk.every
import io.mockk.mockk
import org.bukkit.entity.Player

object PlayerMockGeneration {

    fun aPlayerMock(
        perkPlayerData: PerkData = PerkPlayerDataTestDataGeneration.aPerkPlayerData()
    ): Player = mockk<Player>().apply {
        every { getOrCreatePerkData() } returns perkPlayerData
    }

}
