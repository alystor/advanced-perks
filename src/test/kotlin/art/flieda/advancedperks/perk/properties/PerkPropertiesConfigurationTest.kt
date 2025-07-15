package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.AbstractTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PerkPropertiesConfigurationTest : AbstractTest() {

    @Test
    fun `correctly load perk properties with valid properties file where all nullable fields are null`() {
        val configuration = PerkPropertiesConfiguration("valid_properties")


        val properties = configuration.getProperties()


        properties.enabled shouldBe true
        properties.displayName shouldBe "Valid Perk"
        properties.description shouldBe "Valid Description"
        properties.icon shouldBe "DIRT"
        properties.permission shouldBe null
        properties.price shouldBe null
        properties.disallowedWorlds shouldBe emptyList()
    }

    @Test
    fun `dont load perk properties with invalid properties file where some mandatory fields are missing`() {
        val configuration = PerkPropertiesConfiguration("invalid_properties")
        shouldThrow<IllegalArgumentException> {
            configuration.getProperties()
        }
    }

}
