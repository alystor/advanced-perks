package art.flieda.advancedperks.structure

import art.flieda.advancedperks.perk.properties.PerkProperty
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class PerkPropertyStructureTest {

    private val configurationKeyRegex = Regex("^[a-z_]+$")

    @Test
    fun `every perk properties configuration key only consists of lowercase letters and underscores`() {
        PerkProperty.entries.forEach { perkProperty ->
            withClue("each PerkProperty configuration key is expected to consist only of lowercase letters and underscores violated by '${perkProperty.configurationKey}'") {
                configurationKeyRegex.matches(perkProperty.configurationKey) shouldBe true
            }
        }
    }

}