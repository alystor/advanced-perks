package art.flieda.advancedperks.command.toggle

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.command.subcommands.toggle.ENABLE_PERK_SUB_COMMAND_OTHER_PERMISSION
import art.flieda.advancedperks.command.subcommands.toggle.ENABLE_PERK_SUB_COMMAND_PERMISSION
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class EnablePerkSubCommandTest : AbstractMockBukkitTest() {

    @Test
    fun `do nothing if the command was not used correctly`() {
        val player = server.addPlayer()
            .addPermission(ENABLE_PERK_SUB_COMMAND_PERMISSION)


        player.performCommand("perks enable bird more args that should not exist")
        val message = player.nextMessage()


        player.getOrCreatePerkData().enabledPerks shouldHaveSize 0
        message shouldContain "Incorrect usage"
    }

    @Nested
    inner class EnableWithoutTargetTest {

        @Test
        fun `do not enable the perk when the player does not have the permission for the sub command`() {
            val player = server.addPlayer()


            player.performCommand("perks enable bird")
            val message = player.nextMessage()


            player.getOrCreatePerkData().enabledPerks shouldHaveSize 0
            message shouldContain "permission"
        }

        @Test
        fun `show the player that the perk is already enabled when trying to enable a already enabled perk`() {
            val player = server.addPlayer()
                .addPermission(ENABLE_PERK_SUB_COMMAND_PERMISSION)
                .addPermission("advanced_perks.use.bird")
            val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
            get<PerkStateController>().forceEnablePerk(player, perk)


            player.performCommand("perks enable bird")
            val message = player.nextMessage()


            player.getOrCreatePerkData().enabledPerks shouldContain "bird"
            message shouldContain "already have"
        }

        @Test
        fun `enable the perk when the player has the permission for the sub command and the perk is disabled`() {
            val player = server.addPlayer()
                .addPermission(ENABLE_PERK_SUB_COMMAND_PERMISSION)
                .addPermission("advanced_perks.use.bird")


            player.performCommand("perks enable bird")
            val message = player.nextMessage()


            player.getOrCreatePerkData().enabledPerks shouldContain "bird"
            message shouldContain "enabled"
        }

        @Test
        fun `do nothing if specified perk doesnt exist`() {
            val player = server.addPlayer()
                .addPermission(ENABLE_PERK_SUB_COMMAND_PERMISSION)


            player.performCommand("perks enable absolutely_non_existent_perk")
            val message = player.nextMessage()


            player.getOrCreatePerkData().enabledPerks shouldHaveSize 0
            message shouldContain "was not found"
        }

    }

    @Nested
    inner class EnableWithTargetTest {

        @Test
        fun `perk was forcefully enabled for the target player successfully`() {
            val player = server.addPlayer()
                .addPermission(ENABLE_PERK_SUB_COMMAND_OTHER_PERMISSION)
            val otherPlayer = server.addPlayer()


            player.performCommand("perks enable bird ${otherPlayer.name}")
            val message = player.nextMessage()


            otherPlayer.getOrCreatePerkData().enabledPerks shouldContain "bird"
            message shouldContain "enabled"
        }

        @Test
        fun `do nothing when the player using a target as arg doesnt have the permission to do that`() {
            val player = server.addPlayer()
                .addPermission(ENABLE_PERK_SUB_COMMAND_PERMISSION)
            val otherPlayer = server.addPlayer()


            player.performCommand("perks enable bird ${otherPlayer.name}")
            val message = player.nextMessage()


            otherPlayer.getOrCreatePerkData().enabledPerks shouldHaveSize 0
            message shouldContain "permission"
        }

        @Test
        fun `do nothing when the perk doesnt exist`() {
            val player = server.addPlayer()
                .addPermission(ENABLE_PERK_SUB_COMMAND_OTHER_PERMISSION)
            val otherPlayer = server.addPlayer()


            player.performCommand("perks enable perk_that_does_not_exist ${otherPlayer.name}")
            val message = player.nextMessage()


            otherPlayer.getOrCreatePerkData().enabledPerks shouldHaveSize 0
            message shouldContain "was not found"
        }

        @Test
        fun `do nothing when the player targeted doesnt exist`() {
            val player = server.addPlayer()
                .addPermission(ENABLE_PERK_SUB_COMMAND_OTHER_PERMISSION)


            player.performCommand("perks enable bird playerthatdoesntexist")
            val message = player.nextMessage()


            message shouldContain "was not found"
        }
    }

}
