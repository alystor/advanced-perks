package art.flieda.advancedperks.perk

import art.flieda.advancedperks.perk.properties.DeletePropertyResult
import art.flieda.advancedperks.perk.properties.PerkPropertiesConfiguration
import art.flieda.advancedperks.perk.properties.PerkProperty
import art.flieda.advancedperks.perk.properties.SetPropertyResult
import art.flieda.advancedperks.perk.properties.icon.PerkIcon
import art.flieda.advancedperks.perkdata.getOrCreatePerkData
import org.bukkit.entity.Player
import org.koin.core.component.KoinComponent

abstract class AbstractPerk(
    private val identifier: String,
    private val perkConfiguration: PerkPropertiesConfiguration = PerkPropertiesConfiguration(identifier),
) : Perk, KoinComponent {

    override fun isEnabled(): Boolean = perkConfiguration.getProperties().enabled

    override fun getPerkIcon(): PerkIcon = PerkIcon(perkConfiguration.getProperties())

    override fun getIdentifier(): String = identifier

    override fun getPermission(): String? = perkConfiguration.getProperties().permission

    override fun getDisallowedWorlds(): List<String> = perkConfiguration.getProperties().disallowedWorlds

    override fun getPrice(): Double? = perkConfiguration.getProperties().price

    override fun getDisplayName(): String = perkConfiguration.getProperties().displayName

    override fun deleteProperty(perkProperty: PerkProperty): DeletePropertyResult =
        perkConfiguration.deleteProperty(perkProperty)

    override fun setProperty(perkProperty: PerkProperty, value: Any): SetPropertyResult =
        perkConfiguration.setProperty(perkProperty, value)

    override fun setDisallowedWorlds(worlds: List<String>): SetPropertyResult =
        perkConfiguration.setProperty(PerkProperty.DISALLOWED_WORLDS, worlds)

    internal fun Player?.runWhenPerkIsEnabled(action: (Player) -> Unit) {
        if (this != null && this.getOrCreatePerkData().hasPerkEnabled(this@AbstractPerk)) {
            action(this)
        }
    }

}
