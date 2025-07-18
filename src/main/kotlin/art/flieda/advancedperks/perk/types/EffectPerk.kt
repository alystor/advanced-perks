package art.flieda.advancedperks.perk.types

import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

interface EffectPerk {

    fun getPotionEffectsToApply(): List<PotionEffect>

    fun createPotionEffect(type: PotionEffectType, level: Int): PotionEffect =
        PotionEffect(type, -1, level.minus(1), false, false)

}
