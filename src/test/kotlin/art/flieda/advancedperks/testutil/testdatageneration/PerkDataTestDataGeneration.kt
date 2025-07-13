package art.flieda.advancedperks.testutil.testdatageneration

import art.flieda.advancedperks.perkdata.PerkData
import art.flieda.advancedperks.testutil.testdatageneration.TestDataGeneration.orSometimesEmpty
import java.util.UUID

object PerkDataTestDataGeneration {

    fun aPerkData(
        playerId: UUID = UUID.randomUUID(),
        loadedFromDatasource: Boolean = TestDataGeneration.aBoolean(),
    ) = PerkData(
        playerId = playerId,
        enabledPerks = TestDataGeneration.some { TestDataGeneration.aString(maxLength = 20) }.orSometimesEmpty().toMutableList(),
        boughtPerks = TestDataGeneration.some { TestDataGeneration.aString(maxLength = 20) }.orSometimesEmpty().toMutableList(),
    ).apply {
        this.loadedFromDatasource = loadedFromDatasource
    }

}