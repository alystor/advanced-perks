package art.flieda.advancedperks.command

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.command.subcommands.DELETE_PROPERTY_SUB_COMMAND_PERMISSION
import art.flieda.advancedperks.perk.registry.PerkRegistry
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class DeletePropertySubCommandTest : AbstractMockBukkitTest() {

    @Test
    fun `unable to delete property of perk when the player doesnt have the permission to use the command`() {
        val player = server.addPlayer()


        player.performCommand("perks delete_property bird enabled false")


        player.nextMessage() shouldContain "permission"
    }

    @Test
    fun `successfully deleted property of perk when the command was used correctly`() {
        val player = server.addPlayer().withPermission(DELETE_PROPERTY_SUB_COMMAND_PERMISSION)
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        perk.getPermission() shouldNotBe beNull()


        player.performCommand("perks delete_property bird permission")


        player.nextMessage() shouldContain "deleted the value"
        perk.getPermission() shouldBe beNull()
    }

    @Test
    fun `unable to delete a property when its a mandatory property`() {
        val player = server.addPlayer().withPermission(DELETE_PROPERTY_SUB_COMMAND_PERMISSION)
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!


        player.performCommand("perks delete_property bird display_name")


        player.nextMessage() shouldContain "property is mandatory"
        perk.getDisplayName() shouldNotBe beNull()
    }

    @Test
    fun `unable to delete a property when the specified perk doesnt exist`() {
        val player = server.addPlayer().withPermission(DELETE_PROPERTY_SUB_COMMAND_PERMISSION)


        player.performCommand("perks delete_property perk_that_doesnt_exist price")


        player.nextMessage() shouldContain "perk perk_that_doesnt_exist was not found"
    }

    @Test
    fun `unable to delete a property when the specified property doesnt exist`() {
        val player = server.addPlayer().withPermission(DELETE_PROPERTY_SUB_COMMAND_PERMISSION)


        player.performCommand("perks delete_property bird property_that_doesnt_exist")


        player.nextMessage() shouldContain "property property_that_doesnt_exist was not found"
    }

    @Test
    fun `unable to delete a property when the specified property is not modifiable`() {
        val player = server.addPlayer().withPermission(DELETE_PROPERTY_SUB_COMMAND_PERMISSION)


        player.performCommand("perks delete_property bird enabled")


        player.nextMessage() shouldContain "is not modifiable"
    }

}
