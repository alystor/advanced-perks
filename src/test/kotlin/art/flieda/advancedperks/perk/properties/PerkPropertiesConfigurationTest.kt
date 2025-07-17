package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.AbstractTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PerkPropertiesConfigurationTest : AbstractTest() {

    init {
        addPluginMock()
    }

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
        ) shouldBe PerkSetPropertyResult.INPUT_CONVERSION_FAILED
        configuration.setProperty(PerkProperty.PRICE, false) shouldBe PerkSetPropertyResult.INPUT_CONVERSION_FAILED
    }

    @Test
    fun `fail to set perk property to null when property is marked as mandatory`() {
        val configuration = PerkPropertiesConfiguration("valid_properties")

        configuration.setProperty(PerkProperty.ENABLED, null) shouldBe PerkSetPropertyResult.NULL_ON_MANDATORY_PROPERTY
        configuration.setProperty(PerkProperty.PRICE, null) shouldBe PerkSetPropertyResult.SUCCESS
    }


    @Test
    fun `dont load perk properties with invalid properties file where some mandatory fields are missing`() {
        val configuration = PerkPropertiesConfiguration("invalid_properties")
        shouldThrow<IllegalArgumentException> {
            configuration.getProperties()
        }
    }

}
