package art.flieda.advancedperks.testutil.mockgeneration

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.properties.PerkPropertiesConfiguration
import art.flieda.advancedperks.perk.types.ListenerPerk
import art.flieda.advancedperks.testutil.testdatageneration.TestDataGeneration

object PerkMockGeneration {

    fun aMockedAbstractPerk(
        identifier: String = "mocked_perk",
        enabled: Boolean = TestDataGeneration.aBoolean(),
        config: PerkPropertiesConfiguration = ConfigurationMockGeneration.aPerkPropertiesConfiguration(enabled = enabled),
        isListener: Boolean = false
    ): AbstractPerk = if (isListener) {
        object : AbstractPerk(identifier, config), ListenerPerk {}
    } else {
        object : AbstractPerk(identifier, config) {}
    }
}
