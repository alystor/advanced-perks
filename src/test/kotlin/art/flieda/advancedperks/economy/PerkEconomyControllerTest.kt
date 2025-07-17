package art.flieda.advancedperks.economy

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.economy.adapters.EconomyAdapter
import art.flieda.advancedperks.perk.properties.PerkProperty
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module

class PerkEconomyControllerTest : AbstractMockBukkitTest(module {
    single { TestEconomyAdapter() } bind EconomyAdapter::class
}) {

    @Test
    fun `successfully buy a perk if enough funds are available`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!.apply {
            setProperty(PerkProperty.PRICE, 50)
        }

        val adapter = get<EconomyAdapter>() as TestEconomyAdapter
        adapter.balanceCache.put(player, 100.0)


        val result = get<PerkEconomyController>().buyPerk(player, perk)


        result shouldBe PerkBuyResult.Success
        player.getOrCreatePerkData().hasPerkBought(perk) shouldBe true
    }

    @Test
    fun `return that no price is set when perk doesn't have a price specified`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!


        val result = get<PerkEconomyController>().buyPerk(player, perk) as PerkBuyResult.Restricted


        result.restriction shouldBe PerkBuyRestriction.NO_PRICE_SET
        player.getOrCreatePerkData().hasPerkBought(perk) shouldBe false
    }

    @Test
    fun `return that the player has insufficient funds when perk cost more than the player has in balance`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!.apply {
            setProperty(PerkProperty.PRICE, 999.0)
        }

        val adapter = get<EconomyAdapter>() as TestEconomyAdapter
        adapter.balanceCache.put(player, 10.0)


        val result = get<PerkEconomyController>().buyPerk(player, perk) as PerkBuyResult.Restricted


        result.restriction shouldBe PerkBuyRestriction.INSUFFICIENT_FUNDS
        player.getOrCreatePerkData().hasPerkBought(perk) shouldBe false
    }

    @Test
    fun `return that theres no economy adapter available when the adapter was misconfigured`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!.apply {
            setProperty(PerkProperty.PRICE, 999.0)
        }

        (get<EconomyAdapter>() as TestEconomyAdapter).adapterMisconfigured = true


        val result = get<PerkEconomyController>().buyPerk(player, perk) as PerkBuyResult.Restricted


        result.restriction shouldBe PerkBuyRestriction.NO_ECONOMY_ADAPTER
        player.getOrCreatePerkData().hasPerkBought(perk) shouldBe false
    }

    @Test
    fun `return that there was an error with the transaction when an error occurres during the transaction process`() {
        val player = server.addPlayer()
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!.apply {
            setProperty(PerkProperty.PRICE, 10.0)
        }

        val adapter = get<EconomyAdapter>() as TestEconomyAdapter
        adapter.balanceCache.put(player, 999.0)
        adapter.throwErrorForPlayer.add(player)


        val result = get<PerkEconomyController>().buyPerk(player, perk) as PerkBuyResult.Restricted


        result.restriction shouldBe PerkBuyRestriction.ERROR_DURING_TRANSACTION
        player.getOrCreatePerkData().hasPerkBought(perk) shouldBe false
    }

}
