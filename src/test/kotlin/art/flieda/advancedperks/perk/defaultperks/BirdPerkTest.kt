package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class BirdPerkTest : AbstractMockBukkitTest() {

    @Test
    fun `player is flying after enabling the perk`() {
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        val player = server.addPlayer().apply {
            gameMode = GameMode.SURVIVAL
        }


        get<PerkStateController>().forceEnablePerk(player, perk)


        player.getOrCreatePerkData().hasPerkEnabled(perk) shouldBe true
        player.isFlying shouldBe true
    }

    @Test
    fun `player is not flying anymore after disabling the perk`() {
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        val player = server.addPlayer().apply {
            gameMode = GameMode.SURVIVAL
        }


        get<PerkStateController>().forceEnablePerk(player, perk)
        get<PerkStateController>().forceDisablePerk(player, perk)


        player.getOrCreatePerkData().hasPerkEnabled(perk) shouldBe false
        player.isFlying shouldBe false
    }

    @Test
    fun `player is still flying after disabling the perk when he is in creative or spectator mode but disabled when in survival or adventure`() {
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        val player = server.addPlayer()

        checkFlyStateAfterPerkDisable(player, perk, GameMode.CREATIVE, true)
        checkFlyStateAfterPerkDisable(player, perk, GameMode.SPECTATOR, true)
        checkFlyStateAfterPerkDisable(player, perk, GameMode.SURVIVAL, false)
        checkFlyStateAfterPerkDisable(player, perk, GameMode.ADVENTURE, false)
    }


    @Test
    fun `player is still flying after changing the gamemode`() {
        val perk = get<PerkRegistry>().findPerkByIdentifier("bird")!!
        val player = server.addPlayer()


        get<PerkStateController>().forceEnablePerk(player, perk)


        checkFlyStateAfterGamemodeChange(player, GameMode.CREATIVE)
        checkFlyStateAfterGamemodeChange(player, GameMode.ADVENTURE)
        checkFlyStateAfterGamemodeChange(player, GameMode.SPECTATOR)
        checkFlyStateAfterGamemodeChange(player, GameMode.SURVIVAL)
    }

    private fun checkFlyStateAfterGamemodeChange(player: Player, gameMode: GameMode) {
        player.gameMode = gameMode
        player.isFlying shouldBe true
    }

    private fun checkFlyStateAfterPerkDisable(player: Player, perk: Perk, gameMode: GameMode, expectedState: Boolean) {
        player.isFlying = false
        player.gameMode = gameMode
        get<PerkStateController>().forceEnablePerk(player, perk)
        get<PerkStateController>().forceDisablePerk(player, perk)
        player.isFlying shouldBe expectedState
    }
}
