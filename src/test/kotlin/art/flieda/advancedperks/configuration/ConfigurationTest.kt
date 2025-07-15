package art.flieda.advancedperks.configuration

import art.flieda.advancedperks.AbstractTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class ConfigurationTest : AbstractTest() {

    @Nested
    inner class GetValueTest {

        @Test
        fun `test if configuration correctly loads the values`() {
            val testConfiguration = createConfiguration("configuration/test_configuration.yml")

            testConfiguration.getAny<String>("test_string") shouldBe "Test"
            testConfiguration.getAny<Number>("test_number") shouldBe 420
            testConfiguration.getAny<Boolean>("test_boolean") shouldBe true
            testConfiguration.getListOfAny<String>("test_string_list")!! shouldContainAll listOf(
                "Test1",
                "Test2",
                "Test3"
            )
        }

        @Test
        fun `test if configuration fails to load configuration values that cannot be parse to the requested datatype`() {
            val testConfiguration = createConfiguration("configuration/test_configuration.yml")

            shouldThrow<IllegalArgumentException> {
                testConfiguration.getListOfAny<Number>("test_string")
            }

            shouldThrow<IllegalArgumentException> {
                testConfiguration.getAny<String>("test_string_list")
            }
        }

        @Test
        fun `test if configuration correctly loads the values with nested dots in keys`() {
            val testConfiguration = createConfiguration("configuration/nested_keys_test_configuration.yml")

            testConfiguration.getAny<Number>("depth_of_1") shouldBe 1
            testConfiguration.getAny<Number>("depth_of_2.key") shouldBe 2
            testConfiguration.getAny<Number>("depth_of_3.key.key") shouldBe 3
            testConfiguration.getAny<Number>("depth_of_4.key.key.key") shouldBe 4
        }

    }

    @Nested
    inner class SetValueTest {

        @Test
        fun `test if setting values works correctly and saves to the file`() {
            val firstConfigLoad = createConfiguration("configuration/test_configuration.yml")


            firstConfigLoad.setValue("test_value_to_set", "Test")


            runBlocking {
                firstConfigLoad.saveConfiguration().join()

                val secondConfigLoad = createConfiguration("configuration/test_configuration.yml")

                secondConfigLoad.getAny<String>("test_value_to_set") shouldBe "Test"
            }
        }

        @Test
        fun `test if setting values to null removes them from the configuration`() {
            val firstConfigLoad = createConfiguration("configuration/test_configuration.yml")

            runBlocking {
                firstConfigLoad.getAny<String>("test_string") shouldBe "Test"

                firstConfigLoad.setValue("test_string", null)
                firstConfigLoad.saveConfiguration().join()

                val secondConfigLoad = createConfiguration("configuration/test_configuration.yml")

                secondConfigLoad.getAny<String>("test_string") shouldBe null
            }
        }

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
