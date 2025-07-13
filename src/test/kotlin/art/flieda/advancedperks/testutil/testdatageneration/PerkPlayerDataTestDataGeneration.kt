package art.flieda.advancedperks.testutil.testdatageneration

import art.flieda.advancedperks.core.configuration.SettingConfiguration
import art.flieda.advancedperks.perkdata.PerkData
import io.mockk.mockk
import java.util.UUID

object PerkPlayerDataTestDataGeneration {

    fun aPerkPlayerData(
        enabledPerks: MutableList<String> = mutableListOf()
    ): PerkData = PerkData(
        //settingConfig = mockk<SettingConfiguration>(),
        playerId = UUID.randomUUID(),
        enabledPerks = enabledPerks,
    )

}
