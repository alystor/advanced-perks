package art.flieda.advancedperks.configuration

import art.flieda.advancedperks.AbstractTest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.collections.shouldContainAll
import org.junit.jupiter.api.Test

class MessageConfigurationTest : AbstractTest() {

    @Test
    fun `test if multiple placeholder replacements for message work correctly`() {
        val configuration = createTestMessageConfiguration()


        val unprocessedMessage = configuration.getAny<String>("placeholder_test")
        val message = configuration.getMessage(
            "placeholder_test",
            "test" to "Example Placeholder",
            "test2" to "Another Placeholder"
        )


        unprocessedMessage shouldBe "Placeholder is {test} and {test2}"
        message shouldBe "Placeholder is Example Placeholder and Another Placeholder"
    }

    @Test
    fun `test that color code replacement and color placeholder replacements work correctly`() {
        val configuration = createTestMessageConfiguration()


        val unprocessedMessage = configuration.getAny<String>("color_code_test")
        val message = configuration.getMessage("color_code_test")

        unprocessedMessage shouldBe "&aThis is <#990099>a test for <primary>color codes"
        message shouldBe "§aThis is §x§9§9§0§0§9§9a test for §x§E§6§D§7§F§Fcolor codes"
    }

    @Test
    fun `check if message correctly reflects if no message was found for that key`() {
        val configuration = createTestMessageConfiguration()


        val message = configuration.getMessage("key_does_not_exist")


        message shouldBe "No message found for key: 'key_does_not_exist'"
    }

    @Test
    fun `get a list of messages with working placeholder and color replacement`() {
        val configuration = createTestMessageConfiguration()


        val messageList = configuration.getMessageList("message_list", "example" to "placeholder")


        messageList shouldContainAll listOf("element 1 placeholder", "element §a2", "element 3")
    }

    private fun createTestMessageConfiguration(): MessageConfiguration =
        MessageConfiguration("configuration/test_message_configuration.yml")

}
