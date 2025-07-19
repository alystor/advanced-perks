package art.flieda.advancedperks

import art.flieda.advancedperks.updatechecker.toSemanticVersion
import io.kotest.assertions.withClue
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldNot
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class PluginTest {

    @Nested
    inner class MockBukkitBasedTest : AbstractMockBukkitTest() {

        @Test
        fun `the plugin's version should follow the principles of semantic versioning`() {
            val pluginVersion = plugin.description.version
            withClue("the plugin version '$pluginVersion' should follow valid semantic versioning principles") {
                pluginVersion.toSemanticVersion() shouldNot beNull()
            }
        }

    }

}
