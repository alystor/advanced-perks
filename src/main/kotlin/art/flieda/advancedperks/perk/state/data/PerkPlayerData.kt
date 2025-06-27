package art.flieda.advancedperks.perk.state.data

import art.flieda.advancedperks.core.configuration.SettingConfiguration
import art.flieda.advancedperks.perk.Perk
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.GlobalContext
import java.util.UUID

private const val MAX_ACTIVE_PERKS_PERMISSION_PREFIX = "advanced_perks.max_active_perks."
private const val PERK_DATA_METADATA_KEY = "perk_data"

data class PerkPlayerData(
    private val id: UUID,
    val enabledPerks: MutableList<String> = mutableListOf(),
    val boughtPerks: MutableList<String> = mutableListOf(),
) : KoinComponent {

    val settingConfig = get<SettingConfiguration>()

    val maxActivePerks: Int = calculateMaxActivePerks()
    var loadedFromDatasource = false

    fun mergeLoadedPerkData(loadedPerkData: PerkPlayerData) {
        enabledPerks.addAll(loadedPerkData.enabledPerks)
        boughtPerks.addAll(loadedPerkData.boughtPerks)
        loadedFromDatasource = true
    }

    fun hasPerkEnabled(perk: Perk): Boolean = enabledPerks.contains(perk.getIdentifier())

    //TODO Check if max active perks is performant enough to always invoke for checking
    private fun calculateMaxActivePerks(): Int {
        val player = Bukkit.getPlayer(id) ?: return -1

        return player.effectivePermissions
            .asSequence()
            .mapNotNull { it.permission.removePrefix(MAX_ACTIVE_PERKS_PERMISSION_PREFIX).toIntOrNull() }
            .maxOrNull() ?: settingConfig.getDefaultMaxActivePerks()
    }

}

fun Player.getOrCreatePerkData(): PerkPlayerData {
    return getMetadata(PERK_DATA_METADATA_KEY).firstNotNullOfOrNull { it.value() as? PerkPlayerData } ?: run {
        val perkData = PerkPlayerData(uniqueId)
        setMetadata(PERK_DATA_METADATA_KEY, FixedMetadataValue(GlobalContext.get().get<Plugin>(), perkData))
        perkData
    }
}

fun Player.hasPermissionForPerk(perk: Perk): Boolean =
    perk.getPermission() == null || hasPermission(perk.getPermission()!!) || getOrCreatePerkData().boughtPerks.contains(
        perk.getIdentifier()
    )

//TODO check if this works
fun Player.canEnableMorePerks(): Boolean =
    getOrCreatePerkData().let { perkData -> perkData.enabledPerks.size < perkData.maxActivePerks }

//TODO decide where to put the "run if perk enabled function to require the least parameter possible and
// to make it as generic as possible"