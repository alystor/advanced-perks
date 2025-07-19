package art.flieda.advancedperks.compatitbility.bukkit

import art.flieda.advancedperks.AbstractMockBukkitTest
import io.kotest.matchers.nulls.beNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import org.bukkit.potion.PotionEffectType
import org.junit.jupiter.api.Test

class PotionDeSyncCompatibilityHandlerTest : AbstractMockBukkitTest() {

    @Test
    fun `should prevent removal of perk potion effects but not unrelated ones`() {
        val player = server.addPlayer()
        player.forceEnablePerk("bat")
        player.addPotionEffect(PotionEffectType.WATER_BREATHING.createEffect(-1, 1))

        player.getPotionEffect(PotionEffectType.NIGHT_VISION) shouldNot beNull()
        player.getPotionEffect(PotionEffectType.WATER_BREATHING) shouldNot beNull()


        player.getSimulator().simulateRemovePotionEffect(PotionEffectType.NIGHT_VISION)
        player.getSimulator().simulateRemovePotionEffect(PotionEffectType.WATER_BREATHING)


        player.getPotionEffect(PotionEffectType.NIGHT_VISION) shouldNot beNull()
        player.getPotionEffect(PotionEffectType.WATER_BREATHING) should beNull()
    }

    @Test
    fun `perk potion effects should not be overwritten by weaker or equal effects`() {
        val player = server.addPlayer()
        player.forceEnablePerk("frog")


        player.addPotionEffect(PotionEffectType.JUMP_BOOST.createEffect(-1, 1))


        player.getPotionEffect(PotionEffectType.JUMP_BOOST)!!.amplifier shouldBe 1
    }


    @Test
    fun `should reapply expiring potion effects that are active through a perk`() {
        """
            Theres no real way to test an expiring potion effect in MockBukkit.
        """
    }

}
