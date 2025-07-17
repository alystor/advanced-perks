package art.flieda.advancedperks.perk.registry

import art.flieda.advancedperks.AbstractTest
import art.flieda.advancedperks.perk.types.ListenerPerk
import art.flieda.advancedperks.testutil.mockgeneration.BukkitMockGeneration
import art.flieda.advancedperks.testutil.mockgeneration.PerkMockGeneration
import art.flieda.advancedperks.testutil.testdatageneration.TestDataGeneration
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.verify
import org.bukkit.plugin.Plugin
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class PerkRegistryTest : AbstractTest() {

    @Test
    fun `only find the correct perk by its identifier`() {
        val sut = PerkRegistry()
        (List(5) { TestDataGeneration.aString(maxLength = 15) } + "find_this_identifier").forEach {
            sut.registerPerk(PerkMockGeneration.aMockedAbstractPerk(enabled = true, identifier = it))
        }


        val foundPerk = sut.findPerkByIdentifier("find_this_identifier")


        foundPerk shouldNotBe null
        foundPerk!!.getIdentifier() shouldBe "find_this_identifier"
    }

    @Test
    fun `find no perk if searched for an identifier that for which no perk is registered`() {
        val sut = PerkRegistry()
        TestDataGeneration.some { TestDataGeneration.aString(maxLength = 15) }.forEach {
            sut.registerPerk(PerkMockGeneration.aMockedAbstractPerk(enabled = true, identifier = it))
        }


        val foundPerk = sut.findPerkByIdentifier("find_this_identifier")


        sut.perkCache.size shouldNotBe 0
        foundPerk shouldBe null
    }

    @Test
    fun `dont register perk if its set to disabled`() {
        val sut = PerkRegistry()
        val perk = PerkMockGeneration.aMockedAbstractPerk(enabled = false, identifier = "disabled_perk")


        val result = sut.registerPerk(perk)


        result shouldBe PerkRegisterResult.PERK_DISABLED
        sut.perkCache.size shouldBe 0
    }

    @Test
    fun `dont register perk if another perk with the same identifier was already registered`() {
        val sut = PerkRegistry()
        val perk = PerkMockGeneration.aMockedAbstractPerk(enabled = true, identifier = "disabled_perk")


        val firstResult = sut.registerPerk(perk)
        val secondResult = sut.registerPerk(perk)


        firstResult shouldBe PerkRegisterResult.SUCCESS
        secondResult shouldBe PerkRegisterResult.IDENTIFIER_ALREADY_IN_USE
        sut.perkCache.size shouldBe 1
    }

    @Test
    fun `check if a registered perk that is a ListenerPerk is getting registered as a Listener`() {
        BukkitMockGeneration.runWithBukkitMock { pluginManager ->
            val plugin = get<Plugin>()
            val sut = PerkRegistry()
            val perk =
                PerkMockGeneration.aMockedAbstractPerk(enabled = true, identifier = "listener_perk", isListener = true)


            val result = sut.registerPerk(perk)


            result shouldBe PerkRegisterResult.SUCCESS
            verify(exactly = 1) { pluginManager.registerEvents(perk as ListenerPerk, plugin) }
        }
    }

}
