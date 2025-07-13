package art.flieda.advancedperks.testutil.testdatageneration

import art.flieda.advancedperks.perkdata.PerkData
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
