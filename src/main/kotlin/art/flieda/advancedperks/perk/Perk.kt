package art.flieda.advancedperks.perk

import org.bukkit.entity.Player

interface Perk {

    fun isEnabled(): Boolean

    fun getIdentifier(): String

    fun getPermission(): String?

    fun getDisallowedWorlds(): List<String>

    fun onPerkEnable(player: Player)

    fun onPerkDisable(player: Player)

}