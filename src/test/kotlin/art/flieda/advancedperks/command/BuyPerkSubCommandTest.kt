package art.flieda.advancedperks.command

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.command.subcommands.BUY_PERK_SUB_COMMAND_PERMISSION
import art.flieda.advancedperks.economy.TestEconomyAdapter
import art.flieda.advancedperks.economy.adapters.EconomyAdapter
import art.flieda.advancedperks.perk.properties.PerkProperty
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import org.junit.jupiter.api.Test
import org.koin.core.component.get
import org.koin.dsl.module

class BuyPerkSubCommandTest : AbstractMockBukkitTest(module {
    single<EconomyAdapter> { TestEconomyAdapter() }
}) {

    @Test
    fun `unable to buy perk when the player doesnt have the permission to use the command`() {
        val player = server.addPlayer()


        player.performCommand("perks buy bird")


        player.nextMessage() shouldContain "permission"
        player.getOrCreatePerkData().boughtPerks shouldHaveSize 0
    }

    @Test
    fun `successfully buy a perk using the command if all needed conditions are met`() {
        val player = server.addPlayer().withPermission(BUY_PERK_SUB_COMMAND_PERMISSION)
        val adapter = get<EconomyAdapter>() as TestEconomyAdapter
        adapter.balanceCache.put(player, 100.0)
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!.apply {
            setProperty(PerkProperty.PRICE, 50)
        }


        player.performCommand("perks buy bird")


        player.nextMessage() shouldContain "successfully bought"
        player.getOrCreatePerkData().hasPerkBought(perk) shouldBe true
    }

    @Test
    fun `unable to buy a perk when theres no price set for the perk`() {
        val player = server.addPlayer().withPermission(BUY_PERK_SUB_COMMAND_PERMISSION)


        player.performCommand("perks buy bird")


        player.nextMessage() shouldContain "no price"
        player.getOrCreatePerkData().boughtPerks shouldHaveSize 0
    }

    @Test
    fun `unable to buy a perk when the player has insufficient funds`() {
        val player = server.addPlayer().withPermission(BUY_PERK_SUB_COMMAND_PERMISSION)
        val adapter = get<EconomyAdapter>() as TestEconomyAdapter
        adapter.balanceCache.put(player, 10.0)
        get<PerkRegistry>().findPerkByIdentifier("bird")!!.setProperty(PerkProperty.PRICE, 50)


        player.performCommand("perks buy bird")


        player.nextMessage() shouldContain "insufficient funds"
        player.getOrCreatePerkData().boughtPerks shouldHaveSize 0
    }

    @Test
    fun `unable to buy a perk when theres no economy adapter available`() {
        val player = server.addPlayer().withPermission(BUY_PERK_SUB_COMMAND_PERMISSION)
        val adapter = get<EconomyAdapter>() as TestEconomyAdapter
        adapter.adapterMisconfigured = true
        get<PerkRegistry>().findPerkByIdentifier("bird")!!.setProperty(PerkProperty.PRICE, 50)


        player.performCommand("perks buy bird")


        player.nextMessage() shouldContain "no economy setup"
        player.getOrCreatePerkData().boughtPerks shouldHaveSize 0
    }

    @Test
    fun `unable to buy a perk when an exception occurs during the transaction`() {
        val player = server.addPlayer().withPermission(BUY_PERK_SUB_COMMAND_PERMISSION)
        val adapter = get<EconomyAdapter>() as TestEconomyAdapter
        adapter.balanceCache.put(player, 100.0)
        adapter.throwErrorForPlayer.add(player)
        get<PerkRegistry>().findPerkByIdentifier("bird")!!.setProperty(PerkProperty.PRICE, 50)


        player.performCommand("perks buy bird")


        player.nextMessage() shouldContain "error occurred"
        player.getOrCreatePerkData().boughtPerks shouldHaveSize 0
    }

}
