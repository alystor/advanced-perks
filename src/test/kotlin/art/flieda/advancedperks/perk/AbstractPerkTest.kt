package art.flieda.advancedperks.perk

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perk.registry.PerkRegistry
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class AbstractPerkTest : AbstractMockBukkitTest() {

    private val perkIdentifierRegex = Regex("^[a-z_]+$")

    @Test
    fun `checks that all perks provided by this plugin have identifiers only consisting of lowercase letters and underscores`() {
        get<PerkRegistry>().perkCache.forEach { identifier, perk ->
            perkIdentifierRegex.matches(identifier) shouldBe true
        }
    }

    @Test
    fun `checks that all perks provided by this plugin have identifiers matching the name of its class`() {
        get<PerkRegistry>().perkCache.forEach { identifier, perk ->
            val processedClassName = perk::class.simpleName?.removeSuffix("Perk")
                ?.replace(Regex("([a-z])([A-Z])"), "$1_$2")
                ?.lowercase()
            processedClassName shouldBe identifier
        }
    }

}
