package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.AbstractMockBukkitTest
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.types.EffectPerk
import io.kotest.matchers.collections.shouldContainOnly
import org.mockbukkit.mockbukkit.entity.PlayerMock

abstract class AbstractEffectPerkTest : AbstractMockBukkitTest() {

    internal infix fun PlayerMock.shouldHaveAllPotionEffectsOfPerk(perk: Perk) =
        this.activePotionEffects.map { potionEffect -> potionEffect.type } shouldContainOnly
                (perk as EffectPerk).getPotionEffectsToApply().map { potionEffect -> potionEffect.type }

}
