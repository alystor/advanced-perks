package art.flieda.advancedperks.testutil.testdatageneration

import art.flieda.advancedperks.core.configuration.SettingConfiguration
import art.flieda.advancedperks.perk.state.data.PerkPlayerData
import io.mockk.mockk
import java.util.UUID

object PerkPlayerDataTestDataGeneration {

    fun aPerkPlayerData(
        enabledPerks: MutableList<String> = mutableListOf()
    ): PerkPlayerData = PerkPlayerData(
        settingConfig = mockk<SettingConfiguration>(),
        id = UUID.randomUUID(),
        enabledPerks = enabledPerks,
    )

}
