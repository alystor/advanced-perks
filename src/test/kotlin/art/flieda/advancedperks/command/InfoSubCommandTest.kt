package art.flieda.advancedperks.command

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.command.subcommands.INFO_OTHER_SUB_COMMAND_PERMISSION
import art.flieda.advancedperks.command.subcommands.INFO_SUB_COMMAND_PERMISSION
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class InfoSubCommandTest : AbstractMockBukkitTest() {

    @Test
    fun `do nothing if the command was not used correctly`() {
        val player = server.addPlayer()
            .withPermission(INFO_SUB_COMMAND_PERMISSION)


        player.performCommand("perks info more args than allowed")
        val message = player.nextMessage()


        message shouldContain "Incorrect usage"
    }

    @Nested
    inner class InfoWithoutTargetTest {

        @Test
        fun `success getting info on which perks the player has enabled and bought` ()  {
            val player = server.addPlayer()
                .withPermission(INFO_SUB_COMMAND_PERMISSION)
            val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
            val perk2 = get<PerkRegistry>().findPerkByIdentifier("chicken")!!
            get<PerkStateController>().forceEnablePerk(player, perk)
            get<PerkStateController>().forceEnablePerk(player, perk2)
            player.getOrCreatePerkData().boughtPerks.addAll(listOf("farmer", "firefly"))


            player.performCommand("perks info")
            val message = generateSequence { player.nextMessage() }.toList()


            message.any { it.contains("enabled perks:") } shouldBe true
            message.any { it.contains("bird chicken") } shouldBe true
            message.any { it.contains("bought perks:") } shouldBe true
            message.any { it.contains("farmer firefly") } shouldBe true
        }

        @Test
        fun `unable to execute the command when player is missing the permission` ()  {
            val player = server.addPlayer()


            player.performCommand("perks info")
            val message = player.nextMessage()


            message shouldContain "permission"
        }

    }

    @Nested
    inner class InfoWithTargetTest {

        @Test
        fun `success getting info on another player` () {
            val player = server.addPlayer()
                .withPermission(INFO_OTHER_SUB_COMMAND_PERMISSION)
            val targetPlayer = server.addPlayer("targetPlayer")
            val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
            val perk2 = get<PerkRegistry>().findPerkByIdentifier("chicken")!!
            get<PerkStateController>().forceEnablePerk(targetPlayer, perk)
            get<PerkStateController>().forceEnablePerk(targetPlayer, perk2)
            targetPlayer.getOrCreatePerkData().boughtPerks.addAll(listOf("farmer", "firefly"))


            player.performCommand("perks info ${targetPlayer.name}")
            val message = generateSequence { player.nextMessage() }.toList()


            message.any { it.contains(targetPlayer.name) } shouldBe true
            message.any { it.contains("enabled perks:") } shouldBe true
            message.any { it.contains("bird chicken") } shouldBe true
            message.any { it.contains("bought perks:") } shouldBe true
            message.any { it.contains("farmer firefly") } shouldBe true
        }

        @Test
        fun `unable to execute the command when player is missing the permission` ()  {
            val player = server.addPlayer()
                .withPermission(INFO_SUB_COMMAND_PERMISSION)
            val targetPlayer = server.addPlayer("targetPlayer")


            player.performCommand("perks info ${targetPlayer.name}")
            val message = player.nextMessage()


            message shouldContain "permission"
        }

        @Test
        fun `unable to execute the command when target was not found` ()  {
            val player = server.addPlayer()
                .withPermission(INFO_OTHER_SUB_COMMAND_PERMISSION)


            player.performCommand("perks info playerthatdoesnotexist")
            val message = player.nextMessage()


            message shouldContain "playerthatdoesnotexist was not found"
        }
    }

}
