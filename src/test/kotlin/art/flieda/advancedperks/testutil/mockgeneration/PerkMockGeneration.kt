package art.flieda.advancedperks.testutil.mockgeneration

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.properties.PerkProperties
import art.flieda.advancedperks.perk.properties.PerkPropertiesConfiguration
import art.flieda.advancedperks.testutil.testdatageneration.PerkPropertiesTestDataGeneration

object PerkMockGeneration {

    fun aMockedAbstractPerk(
        properties: PerkProperties = PerkPropertiesTestDataGeneration.aPerkProperties(),
        config: PerkPropertiesConfiguration = ConfigurationMockGeneration.aPerkPropertiesConfiguration(properties)
    ): AbstractPerk = object : AbstractPerk("mocked_perk", config) {}

}
