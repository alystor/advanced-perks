package art.flieda.advancedperks.testutil.mockgeneration

import art.flieda.advancedperks.perk.properties.PerkPropertiesConfiguration
import art.flieda.advancedperks.testutil.testdatageneration.PerkPropertiesTestDataGeneration
import art.flieda.advancedperks.testutil.testdatageneration.TestDataGeneration
import io.mockk.every
import io.mockk.mockk

object ConfigurationMockGeneration {

    fun aPerkPropertiesConfiguration(
        enabled: Boolean = TestDataGeneration.aBoolean()
    ): PerkPropertiesConfiguration =
        mockk<PerkPropertiesConfiguration>().apply {
            every { getProperties() } returns PerkPropertiesTestDataGeneration.aPerkProperties(enabled = enabled)
        }

}
