package art.flieda.advancedperks.perk

interface Perk {

    fun isEnabled(): Boolean

    fun getIdentifier(): String

    fun getPermission(): String?

    fun getPrice(): Double?

    fun getDisallowedWorlds(): List<String>

    fun setDisallowedWorlds(worlds: List<String>)

}
