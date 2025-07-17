package art.flieda.advancedperks.command

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.command.subcommands.SET_PROPERTY_SUB_COMMAND_PERMISSION
import art.flieda.advancedperks.perk.registry.PerkRegistry
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class SetPropertySubCommandTest : AbstractMockBukkitTest() {

    @Test
    fun `unable to set property of perk when the player doesnt have the permission to use the command`() {
        val player = server.addPlayer()


        player.performCommand("perks set_property bird enabled false")


        player.nextMessage() shouldContain "permission"
    }

    @Test
    fun `successfully set property of perk when the command was used correctly`() {
        val player = server.addPlayer().withPermission(SET_PROPERTY_SUB_COMMAND_PERMISSION)
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!


        player.performCommand("perks set_property bird enabled false")


        player.nextMessage() shouldContain "set a new value"
        perk.isEnabled() shouldBe false
    }

    @Test
    fun `successfully set a list property of perk when the command was used correctly`() {
        val player = server.addPlayer().withPermission(SET_PROPERTY_SUB_COMMAND_PERMISSION)
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        val disallowedWorlds = listOf("test_world", "test_the_nether")


        player.performCommand(
            "perks set_property bird disallowed_worlds ${
                jacksonObjectMapper().writeValueAsString(
                    disallowedWorlds
                )
            }"
        )


        player.nextMessage() shouldContain "set a new value"
        perk.getDisallowedWorlds() shouldContainAll disallowedWorlds
    }

    @Test
    fun `unable to set a value for a property where the input cannot be correctly converted`() {
        val player = server.addPlayer().withPermission(SET_PROPERTY_SUB_COMMAND_PERMISSION)
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!


        player.performCommand("perks set_property bird enabled not_a_boolean")


        player.nextMessage() shouldContain "error occurred during the conversion process"
        perk.isEnabled() shouldBe true
    }

    @Test
    fun `unable to set a value for a property when no input is specified`() {
        val player = server.addPlayer().withPermission(SET_PROPERTY_SUB_COMMAND_PERMISSION)
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!


        player.performCommand("perks set_property bird enabled")


        player.nextMessage() shouldContain "Incorrect usage"
        perk.isEnabled() shouldBe true
    }

    @Test
    fun `unable to set a value for a property when the specified perk doesnt exist`() {
        val player = server.addPlayer().withPermission(SET_PROPERTY_SUB_COMMAND_PERMISSION)


        player.performCommand("perks set_property perk_that_doesnt_exist enabled false")


        player.nextMessage() shouldContain "perk perk_that_doesnt_exist was not found"
    }

    @Test
    fun `unable to set a value for a property when the specified property doesnt exist`() {
        val player = server.addPlayer().withPermission(SET_PROPERTY_SUB_COMMAND_PERMISSION)


        player.performCommand("perks set_property bird property_that_doesnt_exist false")


        player.nextMessage() shouldContain "property property_that_doesnt_exist was not found"
    }

}
