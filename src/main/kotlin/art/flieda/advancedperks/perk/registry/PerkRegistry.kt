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

    fun registerPerk(perk: Perk): PerkRegisterResult =
        when {
            findPerkByIdentifier(perk.getIdentifier()) != null -> {
                val existingPerk = findPerkByIdentifier(perk.getIdentifier())!!
                logger.warning { "Perk '${perk.javaClass.name}' cannot be registered because its chosen identifier '${perk.getIdentifier()}' is already in use by '${existingPerk.javaClass.name}'." }
                PerkRegisterResult.IDENTIFIER_ALREADY_IN_USE
            }

            !perk.isEnabled() -> {
                logger.fine { "Perk '${perk.javaClass.name}' wasn't registered because it is set to disabled." }
                PerkRegisterResult.PERK_DISABLED
            }

            else -> {
                if (perk is ListenerPerk) {
                    Bukkit.getPluginManager().registerEvents(perk, plugin)
                    logger.fine { "Successfully registered perk class '${perk.javaClass.name}' as an event listener." }
                }

                perkCache[perk.getIdentifier()] = perk
                logger.fine { "Perk '${perk.javaClass}' was registered successfully." }
                PerkRegisterResult.SUCCESS
            }
        }

    private fun loadPerksAndRegister() {
        val perkInstances = getKoin().getAll<Perk>()
        perkInstances.forEach { perk -> registerPerk(perk) }
    }

}
