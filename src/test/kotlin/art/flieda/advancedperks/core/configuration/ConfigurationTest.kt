package art.flieda.advancedperks.core.configuration

import art.flieda.advancedperks.AbstractTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ConfigurationTest : AbstractTest() {

    @Test
    fun `test if configuration correctly loads the values`() {
        val testConfiguration = createConfiguration("configuration/test_configuration.yml")

        testConfiguration.getString("test_string") shouldBe "Test"
        testConfiguration.getNumber("test_number") shouldBe 420
        testConfiguration.getBoolean("test_boolean") shouldBe true
        testConfiguration.getStringList("test_string_list")!! shouldContainAll listOf("Test1", "Test2", "Test3")
    }

    @Test
    fun `test if configuration correctly loads the values with nested dots in keys`() {
        val testConfiguration = createConfiguration("configuration/nested_keys_test_configuration.yml")

        testConfiguration.getNumber("depth_of_1") shouldBe 1
        testConfiguration.getNumber("depth_of_2.key") shouldBe 2
        testConfiguration.getNumber("depth_of_3.key.key") shouldBe 3
        testConfiguration.getNumber("depth_of_4.key.key.key") shouldBe 4
    }

    @Test
    fun `test if setting values works correctly and saves to the file`() {
        val firstConfigLoad = createConfiguration("configuration/test_configuration.yml")


        firstConfigLoad.setValue("test_value_to_set", "Test")
        firstConfigLoad.saveConfiguration()


        val secondConfigLoad = object : Configuration(
            "configuration/test_configuration.yml"
        ) {}
        secondConfigLoad.getString("test_value_to_set") shouldBe "Test"
    }

    @Test
    fun `fail configuration initialization if no file is found on the classpath`() {
        shouldThrow<IllegalStateException> {
            createConfiguration("this_configuration_does_not_exist.yml")
        }
    }

    @Test
    fun `fail configuration initialization if config file has an incorrect yaml format`() {
        shouldThrow<IllegalStateException> {
            createConfiguration("incorrect_configuration.yml")
        }
    }

    private fun createConfiguration(classPathUrl: String) = object : Configuration(
        classPathUrl
    ) {}

}
