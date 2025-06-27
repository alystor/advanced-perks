package art.flieda.advancedperks.perk.registry

import art.flieda.advancedperks.AdvancedPerks.Companion.logger
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class PerkRegistry : KoinComponent {

    val plugin = get<Plugin>()
    val perkCache = mutableMapOf<String, Perk>()

    init {
        logger.info { "PerkRegistry loading and registering perks..." }
        loadPerksAndRegister()
        logger.info { "Perk Registry finished loading and registering perks." }
    }

    fun findPerkByIdentifier(identifier: String): Perk? = perkCache[identifier]

    fun registerPerk(perk: Perk): PerkRegisterResult {
        findPerkByIdentifier(perk.getIdentifier())?.let {
            logger.warning { "Perk '${perk.javaClass.name}' cannot be registered because its chosen identifier '${perk.getIdentifier()}' is already in use by '${it.javaClass.name}'." }
            return PerkRegisterResult.IDENTIFIER_ALREADY_IN_USE
        }

        if (!perk.isEnabled()) {
            logger.fine { "Perk '${perk.javaClass.name}' wasn't registered because it is set to disabled." }
            return PerkRegisterResult.PERK_DISABLED
        }

        if (perk is ListenerPerk) {
            Bukkit.getPluginManager().registerEvents(perk, plugin)
            logger.fine { "Successfully registered perk class '${perk.javaClass.name}' as a event listener." }
        }

        perkCache[perk.getIdentifier()] = perk
        logger.fine { "Perk '${perk.javaClass}' was registered successfully. " }
        return PerkRegisterResult.SUCCESS
    }

    private fun loadPerksAndRegister() {
        val perkInstances = getKoin().getAll<Perk>()
        perkInstances.forEach { perk -> registerPerk(perk) }
    }

}