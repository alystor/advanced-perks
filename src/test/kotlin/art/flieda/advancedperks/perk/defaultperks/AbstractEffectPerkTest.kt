package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.types.EffectPerk
import io.kotest.matchers.collections.shouldContainOnly
import io.kotest.matchers.shouldBe
import org.mockbukkit.mockbukkit.entity.PlayerMock

//TODO add tests for all of the effect desync stuff
abstract class AbstractEffectPerkTest : AbstractMockBukkitTest() {

    internal fun testPotionEffectOnEnableAndDisable(perkIdentifier: String) {
        val player = server.addPlayer()


        val perk = player.forceEnablePerk(perkIdentifier)
        player shouldHaveAllPotionEffectsOfPerk perk


        player.forceDisablePerk(perkIdentifier)
        player.activePotionEffects.isEmpty() shouldBe true
    }

    internal infix fun PlayerMock.shouldHaveAllPotionEffectsOfPerk(perk: Perk) =
        this.activePotionEffects.map { potionEffect -> potionEffect.type } shouldContainOnly
                (perk as EffectPerk).getPotionEffectsToApply().map { potionEffect -> potionEffect.type }

}
