package art.flieda.advancedperks.perk

import art.flieda.advancedperks.perk.properties.PerkProperty

interface Perk {

    fun isEnabled(): Boolean

    fun getIdentifier(): String

    fun getPermission(): String?

    fun getPrice(): Double?

    fun getDisallowedWorlds(): List<String>

    fun setProperty(perkProperty: PerkProperty, value: Any?)

    fun setDisallowedWorlds(worlds: List<String>)

}
