package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.EffectPerk
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class BerserkerPerk : AbstractPerk("berserker"), EffectPerk {

    override fun getPotionEffectsToApply(): List<PotionEffect> = listOf(
        createPotionEffect(
            PotionEffectType.getByName("INCREASE_DAMAGE")
                ?: PotionEffectType.getByName("STRENGTH")
                ?: error("No suitable jump potion effect found!"),
            1
        )
    )

}
