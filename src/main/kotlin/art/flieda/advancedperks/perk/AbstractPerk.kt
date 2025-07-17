package art.flieda.advancedperks.perk

import art.flieda.advancedperks.perk.properties.PerkPropertiesConfiguration
import art.flieda.advancedperks.perk.properties.PerkProperty
import art.flieda.advancedperks.perk.properties.PerkSetPropertyResult
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent

abstract class AbstractPerk(
    private val identifier: String,
    private val perkConfiguration: PerkPropertiesConfiguration = PerkPropertiesConfiguration(identifier),
) : Perk, KoinComponent {

    override fun isEnabled(): Boolean = perkConfiguration.getProperties().enabled

    override fun getIdentifier(): String = identifier

    override fun getPermission(): String? = perkConfiguration.getProperties().permission

    override fun getDisallowedWorlds(): List<String> = perkConfiguration.getProperties().disallowedWorlds

    override fun getPrice(): Double? = perkConfiguration.getProperties().price

    override fun getDisplayName(): String = perkConfiguration.getProperties().displayName

    override fun setProperty(perkProperty: PerkProperty, value: Any): PerkSetPropertyResult =
        perkConfiguration.setProperty(perkProperty, value)

    override fun setDisallowedWorlds(worlds: List<String>): PerkSetPropertyResult =
        perkConfiguration.setProperty(PerkProperty.DISALLOWED_WORLDS, worlds)

    fun runWhenPerkIsEnabled(player: Player, action: (Player) -> Unit) = takeIf { isEnabled() }?.let { action(player) }

}
