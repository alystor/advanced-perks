package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.EffectPerk
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FireflyPerk : AbstractPerk("firefly"), EffectPerk {

    override fun getPotionEffectsToApply(): List<PotionEffect> = listOf(
        createPotionEffect(PotionEffectType.GLOWING, 1)
    )

}
