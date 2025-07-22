package art.flieda.advancedperks.perkdata

import art.flieda.advancedperks.configuration.SettingConfiguration
import art.flieda.advancedperks.perk.Perk
import art.flieda.advancedperks.perk.registry.PerkRegistry
import art.flieda.advancedperks.perkdata.repository.PerkDataProjection
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

data class PerkData(
    internal val playerId: UUID,
    internal val enabledPerks: MutableSet<String> = mutableSetOf(),
    internal val boughtPerks: MutableSet<String> = mutableSetOf(),
) : KoinComponent {

    val perkRegistry by lazy { get<PerkRegistry>() }
    val configuration = get<SettingConfiguration>()
    var loadedFromDatasource = false

    fun mergeWithLoadedProjection(projection: PerkDataProjection) {
        check(playerId == projection.playerId) {
            "Cannot merge PerkDataProjection with the playerId '${projection.playerId}' with the PerkData with the playerId '$playerId'"
        }
        enabledPerks.addAll(projection.enabledPerks)
        boughtPerks.addAll(projection.boughtPerks)
        loadedFromDatasource = true
    }

    fun getEnabledAndLoadedPerks(): List<Perk> =
        enabledPerks.mapNotNull { perkIdentifier -> perkRegistry.findPerkByIdentifier(perkIdentifier) }

    fun hasPerkBought(perk: Perk): Boolean = boughtPerks.contains(perk.getIdentifier())

    fun hasPerkEnabled(perk: Perk): Boolean = enabledPerks.contains(perk.getIdentifier())

    fun getMaxActivePerksAllowed(): Int {
        val player = Bukkit.getPlayer(playerId) ?: return -1

        return player.effectivePermissions
            .asSequence()
            .mapNotNull { it.permission.removePrefix(MAX_ACTIVE_PERKS_PERMISSION_PREFIX).toIntOrNull() }
            .maxOrNull() ?: configuration.getDefaultMaxActivePerks()
    }

}

fun Player.removePerkData() = removeMetadata(PERK_DATA_METADATA_KEY, GlobalContext.get().get<Plugin>())

fun Player.getOrCreatePerkData(): PerkData =
    getMetadata(PERK_DATA_METADATA_KEY).firstNotNullOfOrNull { it.value() as? PerkData } ?: let {
        val perkData = PerkData(playerId = uniqueId)
        setMetadata(PERK_DATA_METADATA_KEY, FixedMetadataValue(GlobalContext.get().get<Plugin>(), perkData))
        perkData
    }

fun Player.hasPermissionToUsePerk(perk: Perk) =
    perk.getPermission() == null || hasPermission(requireNotNull(perk.getPermission())) || getOrCreatePerkData().boughtPerks.contains(
        perk.getIdentifier()
    )

fun Player.isAllowedToEnableMorePerks(): Boolean =
    getOrCreatePerkData().let { perkData -> perkData.enabledPerks.size < perkData.getMaxActivePerksAllowed() }

inline fun <reified T : Any> List<Perk>.onlyFromType(): List<T> = this.filterIsInstance<T>()
