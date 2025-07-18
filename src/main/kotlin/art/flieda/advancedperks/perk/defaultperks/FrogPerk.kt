package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.EffectPerk
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FrogPerk : AbstractPerk("frog"), EffectPerk {

    override fun getPotionEffectsToApply(): List<PotionEffect> = listOf(
        createPotionEffect(
            PotionEffectType.getByName("JUMP_BOOST")
                ?: PotionEffectType.getByName("JUMP")
                ?: error("No suitable jump potion effect found!"),
            2
        )
    )

}
