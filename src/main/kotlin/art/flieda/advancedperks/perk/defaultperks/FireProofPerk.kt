package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.EffectPerk
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FireProofPerk : AbstractPerk("fire_proof"), EffectPerk {

    override fun getPotionEffectsToApply(): List<PotionEffect> = listOf(
        createPotionEffect(PotionEffectType.FIRE_RESISTANCE, 1)
    )

}
