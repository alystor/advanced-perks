package art.flieda.advancedperks.perk

import art.flieda.advancedperks.perk.properties.DeletePropertyResult
import art.flieda.advancedperks.perk.properties.PerkProperty
import art.flieda.advancedperks.perk.properties.SetPropertyResult
import art.flieda.advancedperks.perk.properties.icon.PerkIcon

interface Perk {

    fun isEnabled(): Boolean

    fun getPerkIcon(): PerkIcon

    fun getIdentifier(): String

    fun getPermission(): String?

    fun getPrice(): Double?

    fun getDisallowedWorlds(): List<String>

    fun getDisplayName(): String

    fun deleteProperty(perkProperty: PerkProperty): DeletePropertyResult

    fun setProperty(perkProperty: PerkProperty, value: Any): SetPropertyResult

    fun setDisallowedWorlds(worlds: List<String>): SetPropertyResult

}
