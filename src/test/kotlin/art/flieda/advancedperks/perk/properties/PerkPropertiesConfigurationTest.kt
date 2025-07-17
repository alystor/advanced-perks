package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.AbstractTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PerkPropertiesConfigurationTest : AbstractTest() {

    @Nested
    inner class LoadPropertyTest {

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

    @Nested
    inner class SetPropertyTest {

        @Test
        fun `correctly set perk property when valid value is provided`() {
            val configuration = PerkPropertiesConfiguration("valid_properties")

            configuration.setProperty(PerkProperty.ENABLED, false)
            configuration.setProperty(PerkProperty.DISPLAY_NAME, "New Display Name")
            configuration.setProperty(PerkProperty.DESCRIPTION, "New Description")
            configuration.setProperty(PerkProperty.PERMISSION, "new.permission")
            configuration.setProperty(PerkProperty.PRICE, 30)
            configuration.setProperty(PerkProperty.DISALLOWED_WORLDS, listOf("WORLD1", "WORLD2"))


            val propertiesAfter = configuration.getProperties()


            propertiesAfter.enabled shouldBe false
            propertiesAfter.displayName shouldBe "New Display Name"
            propertiesAfter.description shouldBe "New Description"
            propertiesAfter.permission shouldBe "new.permission"
            propertiesAfter.price shouldBe 30
            propertiesAfter.disallowedWorlds shouldContainAll listOf("WORLD1", "WORLD2")
        }

        @Test
        fun `fail to set perk property when value with incompatible datatype is provided`() {
            val configuration = PerkPropertiesConfiguration("valid_properties")

            configuration.setProperty(
                PerkProperty.ENABLED,
                "INCOMPATIBLE"
            ) shouldBe SetPropertyResult.INPUT_CONVERSION_FAILED
            configuration.setProperty(PerkProperty.PRICE, false) shouldBe SetPropertyResult.INPUT_CONVERSION_FAILED
        }

    }

    @Nested
    inner class DeletePropertyTest {

        @Test
        fun `successfully deleted all non-mandatory properties`() {
            val configuration = PerkPropertiesConfiguration("valid_properties_with_all_fields_set")


            configuration.deleteProperty(PerkProperty.PERMISSION)
            configuration.deleteProperty(PerkProperty.PRICE)
            configuration.deleteProperty(PerkProperty.DISALLOWED_WORLDS)

            configuration.getProperties().permission shouldBe beNull()
            configuration.getProperties().price shouldBe beNull()
            configuration.getProperties().disallowedWorlds shouldHaveSize 0
        }

        @Test
        fun `unable to delete properties that are mandatory`() {
            val configuration = PerkPropertiesConfiguration("valid_properties")


            configuration.deleteProperty(PerkProperty.ENABLED) shouldBe DeletePropertyResult.PROPERTY_IS_MANDATORY
            configuration.deleteProperty(PerkProperty.DISPLAY_NAME) shouldBe DeletePropertyResult.PROPERTY_IS_MANDATORY
            configuration.deleteProperty(PerkProperty.DESCRIPTION) shouldBe DeletePropertyResult.PROPERTY_IS_MANDATORY
            configuration.deleteProperty(PerkProperty.ICON) shouldBe DeletePropertyResult.PROPERTY_IS_MANDATORY


            configuration.getProperties().enabled shouldNotBe beNull()
            configuration.getProperties().displayName shouldNotBe beNull()
            configuration.getProperties().description shouldNotBe beNull()
            configuration.getProperties().icon shouldNotBe beNull()
        }

    }

}
