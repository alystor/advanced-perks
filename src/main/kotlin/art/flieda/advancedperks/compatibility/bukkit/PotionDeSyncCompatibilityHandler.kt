package art.flieda.advancedperks.compatibility.bukkit

import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perk.types.EffectPerk
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import art.flieda.advancedperks.perkdata.onlyFromType
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PotionDeSyncCompatibilityHandler : Listener, KoinComponent {

    val perkRegistry = get<PerkRegistry>()
    val plugin = get<Plugin>()

    init {
        Bukkit.getPluginManager().registerEvents(this, plugin)
    }

    @EventHandler
    fun onPlayerRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        player.getOrCreatePerkData().getEnabledAndLoadedPerks().onlyFromType<EffectPerk>()
            .flatMap { effectPerk -> effectPerk.getPotionEffectsToApply() }
            .forEach { potionEffect -> player.addPotionEffect(potionEffect) }
    }

    @EventHandler
    fun onEntityPotionEffect(event: EntityPotionEffectEvent) {
        (event.entity as? Player)?.let { player ->
            if (event.action != EntityPotionEffectEvent.Action.ADDED) {
                println(event.cause)
                player.getOrCreatePerkData().getEnabledAndLoadedPerks().onlyFromType<EffectPerk>()
                    .flatMap { it.getPotionEffectsToApply() }
                    .find { potionEffect -> potionEffect.type == event.oldEffect?.type }
                    ?.let { potionEffect ->
                        when (event.action) {
                            EntityPotionEffectEvent.Action.REMOVED, EntityPotionEffectEvent.Action.CLEARED -> {
                                player.handlePotionRemoveOrClear(potionEffect, event)
                            }

                            EntityPotionEffectEvent.Action.CHANGED -> {
                                handlePotionChange(potionEffect, event)
                            }

                            else -> {}
                        }
                    }
            }
        }
    }

    private fun handlePotionChange(
        perkPotionEffect: PotionEffect, event: EntityPotionEffectEvent
    ) {
        event.newEffect?.let { newPotionEffect ->
            event.isCancelled = newPotionEffect.amplifier <= perkPotionEffect.amplifier
        }
    }

    private fun Player.handlePotionRemoveOrClear(
        perkPotionEffect: PotionEffect, event: EntityPotionEffectEvent
    ) {
        if (event.cause == EntityPotionEffectEvent.Cause.EXPIRATION) {
            this.addPotionEffect(perkPotionEffect)
        } else {
            event.isCancelled = true
        }
    }

}
