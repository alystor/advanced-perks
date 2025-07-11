package art.flieda.advancedperks.testutil.testdatageneration

import art.flieda.advancedperks.perk.properties.PerkProperties
import art.flieda.advancedperks.testutil.testdatageneration.TestDataGeneration.orSometimesNull
import art.flieda.advancedperks.testutil.testdatageneration.TestDataGeneration.orUsuallyNull
import org.bukkit.Material

object PerkPropertiesTestDataGeneration {

    fun aPerkProperties(
        enabled: Boolean = TestDataGeneration.aBoolean(),
    ) = PerkProperties(
        enabled = enabled,
        displayName = TestDataGeneration.faker.name().fullName(),
        description = TestDataGeneration.faker.lorem().paragraph(),
        icon = TestDataGeneration.aRandomEnumValue<Material>().name,
        permission = TestDataGeneration.faker.name().firstName().orSometimesNull(),
        price = TestDataGeneration.aPositiveDouble().orUsuallyNull(),
        disallowedWorlds = TestDataGeneration.some { TestDataGeneration.aString(16) }
    )

}
