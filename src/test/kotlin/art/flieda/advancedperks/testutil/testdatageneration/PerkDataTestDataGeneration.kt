package art.flieda.advancedperks.testutil.testdatageneration

import art.flieda.advancedperks.perkdata.PerkData
import art.flieda.advancedperks.testutil.testdatageneration.TestDataGeneration.orSometimesEmpty
import java.util.UUID

object PerkDataTestDataGeneration {

    fun aPerkData(
        playerId: UUID = UUID.randomUUID(),
        enabledPerks: MutableList<String> = TestDataGeneration.some { TestDataGeneration.aString(maxLength = 20) }
            .orSometimesEmpty().toMutableList(),
        boughtPerks: MutableList<String> = TestDataGeneration.some { TestDataGeneration.aString(maxLength = 20) }
            .orSometimesEmpty().toMutableList(),
        loadedFromDatasource: Boolean = TestDataGeneration.aBoolean(),
    ) = PerkData(
        playerId = playerId, enabledPerks = enabledPerks, boughtPerks = boughtPerks
    ).apply {
        this.loadedFromDatasource = loadedFromDatasource
    }

    fun aPerkDataWithEnabledAndBoughtPerks(
        playerId: UUID = UUID.randomUUID(),
    ) = aPerkData(
        playerId = playerId,
        enabledPerks = TestDataGeneration.some { TestDataGeneration.aString(maxLength = 20) }.toMutableList(),
        boughtPerks = TestDataGeneration.some { TestDataGeneration.aString(maxLength = 20) }.toMutableList(),
    )

}
