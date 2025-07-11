package art.flieda.advancedperks.testutil.mockgeneration

import art.flieda.advancedperks.perk.properties.PerkProperties
import art.flieda.advancedperks.perk.properties.PerkPropertiesConfiguration
import art.flieda.advancedperks.testutil.testdatageneration.PerkPropertiesTestDataGeneration
import io.mockk.every
import io.mockk.mockk

object ConfigurationMockGeneration {

    fun aPerkPropertiesConfiguration(
        properties: PerkProperties = PerkPropertiesTestDataGeneration.aPerkProperties()
    ): PerkPropertiesConfiguration =
        mockk<PerkPropertiesConfiguration>().apply {
            every { getProperties() } returns properties
        }

}
