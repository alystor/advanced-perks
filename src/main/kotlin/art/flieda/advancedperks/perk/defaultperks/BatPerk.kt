package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.EffectPerk
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class BatPerk : AbstractPerk("bat"), EffectPerk {

    override fun getPotionEffectsToApply(): List<PotionEffect> = listOf(
        createPotionEffect(PotionEffectType.NIGHT_VISION, 1)
    )

}
