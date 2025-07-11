package art.flieda.advancedperks.perk

import art.flieda.advancedperks.perk.properties.PerkPropertiesConfiguration
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent

abstract class AbstractPerk(
    private val identifier: String
) : Perk, KoinComponent {

    val perkConfiguration = PerkPropertiesConfiguration(identifier)
    val properties = perkConfiguration.getProperties()

    override fun isEnabled(): Boolean = properties.enabled

    override fun getIdentifier(): String = identifier

    override fun getPermission(): String? = properties.permission

    override fun getDisallowedWorlds(): List<String> = properties.disallowedWorlds

    fun runWhenPerkIsEnabled(player: Player, action: (Player) -> Unit) = takeIf { isEnabled() }?.let { action(player) }

}
