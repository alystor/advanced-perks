package art.flieda.advancedperks.perk

import art.flieda.advancedperks.perk.properties.PerkProperty
import art.flieda.advancedperks.perk.properties.PerkSetPropertyResult

interface Perk {

    fun isEnabled(): Boolean

    fun getIdentifier(): String

    fun getPermission(): String?

    fun getPrice(): Double?

    fun getDisallowedWorlds(): List<String>

    fun getDisplayName(): String

    fun setProperty(perkProperty: PerkProperty, value: Any): PerkSetPropertyResult

    fun setDisallowedWorlds(worlds: List<String>): PerkSetPropertyResult

}
