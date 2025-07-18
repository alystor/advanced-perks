package art.flieda.advancedperks.testutil.mockbukkit

import org.bukkit.Bukkit
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.potion.PotionEffectType
import org.mockbukkit.mockbukkit.entity.PlayerMock
import org.mockbukkit.mockbukkit.simulate.entity.PlayerSimulation

class ExtendedPlayerSimulation(val playerMock: PlayerMock) : PlayerSimulation(playerMock) {

    fun simulateRemovePotionEffect(potionEffectType: PotionEffectType): EntityPotionEffectEvent? =
        playerMock.getPotionEffect(potionEffectType)?.let { potionEffect ->
            val entityPotionEvent = EntityPotionEffectEvent(
                playerMock,
                potionEffect,
                null,
                EntityPotionEffectEvent.Cause.PLUGIN,
                EntityPotionEffectEvent.Action.REMOVED,
                false
            )

            Bukkit.getPluginManager().callEvent(entityPotionEvent)
            if (entityPotionEvent.isCancelled.not()) {
                playerMock.removePotionEffect(potionEffectType)
            }
            entityPotionEvent
        }

}
