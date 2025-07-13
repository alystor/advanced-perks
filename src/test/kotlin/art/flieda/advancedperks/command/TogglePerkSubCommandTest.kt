package art.flieda.advancedperks.command

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.command.subcommands.TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION
import art.flieda.advancedperks.command.subcommands.TOGGLE_PERK_SUB_COMMAND_PERMISSION
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class TogglePerkSubCommandTest : AbstractMockBukkitTest() {

    @Test
    fun `do nothing if the command was not used correctly`() {
        val player = server.addPlayer()
            .addPermission(TOGGLE_PERK_SUB_COMMAND_PERMISSION)


        player.performCommand("perks toggle bird more args that should not exist")
        val message = player.nextMessage()


        player.getOrCreatePerkData().enabledPerks.isEmpty() shouldBe true
        message shouldContain "Incorrect usage"
    }

    @Nested
    inner class ToggleWithoutTargetTest {

        @Test
        fun `do not toggle the perk when the player does not have the permission for the sub command`() {
            val player = server.addPlayer()
                .addPermission(TOGGLE_PERK_SUB_COMMAND_PERMISSION)


            player.performCommand("perks toggle bird")
            val message = player.nextMessage()


            player.getOrCreatePerkData().enabledPerks.isEmpty() shouldBe true
            message shouldContain "permission"
        }

        @Test
        fun `enable the perk when the player has the permission for the sub command and the perk is disabled`() {
            val player = server.addPlayer()
                .addPermission(TOGGLE_PERK_SUB_COMMAND_PERMISSION)
                .addPermission("advanced_perks.use.bird")


            player.performCommand("perks toggle bird")
            val message = player.nextMessage()


            player.getOrCreatePerkData().enabledPerks.contains("bird") shouldBe true
            message shouldContain "enabled"
        }

        @Test
        fun `disable the perk when the player has the permission for the sub command and the perk is enabled`() {
            val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
            val player = server.addPlayer()
                .addPermission(TOGGLE_PERK_SUB_COMMAND_PERMISSION)
                .addPermission("advanced_perks.use.bird")


            get<PerkStateController>().forceEnablePerk(player, perk)
            player.performCommand("perks toggle bird")
            val message = player.nextMessage()


            player.getOrCreatePerkData().enabledPerks.isEmpty() shouldBe true
            message shouldContain "disabled"
        }

        @Test
        fun `do nothing if specified perk doesnt exist`() {
            val player = server.addPlayer()
                .addPermission(TOGGLE_PERK_SUB_COMMAND_PERMISSION)


            player.performCommand("perks toggle absolutely_non_existent_perk")
            val message = player.nextMessage()


            player.getOrCreatePerkData().enabledPerks.isEmpty() shouldBe true
            message shouldContain "was not found"
        }

    }

    @Nested
    inner class ToggleWithTargetTest {

        @Test
        fun `perk was forcefully toggled for the target player successfully`() {
            val player = server.addPlayer()
                .addPermission(TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION)
            val otherPlayer = server.addPlayer()


            player.performCommand("perks toggle bird ${otherPlayer.name}")
            val message = player.nextMessage()


            otherPlayer.getOrCreatePerkData().enabledPerks.contains("bird") shouldBe true
            message shouldContain "enabled"
        }

        @Test
        fun `do nothing when the player using a target as arg doesnt have the permission to do that`() {
            val player = server.addPlayer()
                .addPermission(TOGGLE_PERK_SUB_COMMAND_PERMISSION)
            val otherPlayer = server.addPlayer()


            player.performCommand("perks toggle bird ${otherPlayer.name}")
            val message = player.nextMessage()


            otherPlayer.getOrCreatePerkData().enabledPerks.isEmpty() shouldBe true
            message shouldContain "permission"
        }

        @Test
        fun `do nothing when the perk doesnt exist`() {
            val player = server.addPlayer()
                .addPermission(TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION)
            val otherPlayer = server.addPlayer()


            player.performCommand("perks toggle perk_that_does_not_exist ${otherPlayer.name}")
            val message = player.nextMessage()


            message shouldContain "was not found"
        }

        @Test
        fun `do nothing when the player targeted doesnt exist`() {
            val player = server.addPlayer()
                .addPermission(TOGGLE_PERK_SUB_COMMAND_OTHER_PERMISSION)


            player.performCommand("perks toggle bird playerthatdoesntexist")
            val message = player.nextMessage()


            message shouldContain "was not found"
        }
    }

}
