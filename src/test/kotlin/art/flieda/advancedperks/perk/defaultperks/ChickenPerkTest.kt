package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.state.PerkStateController
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import io.kotest.matchers.shouldBe
import org.bukkit.GameMode
import org.junit.jupiter.api.Test
import org.koin.core.component.get

class ChickenPerkTest : AbstractMockBukkitTest() {

    @Test
    fun `player is not taking fall damage when this perk is enabled`() {
        val perk = get<PerkRegistry>().findPerkByIdentifier("chicken")!!
        val player = server.addPlayer().apply {
            gameMode = GameMode.SURVIVAL
        }


        get<PerkStateController>().forceEnablePerk(player, perk)
        player.applyFallDamage(5.0)


        player.health shouldBe 20.0
        player.getOrCreatePerkData().hasPerkEnabled(perk) shouldBe true
    }

}
