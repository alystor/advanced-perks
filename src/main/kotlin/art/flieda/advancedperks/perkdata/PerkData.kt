package art.flieda.advancedperks.perkdata

import art.flieda.advancedperks.perkdata.repository.PerkDataProjection
import org.bukkit.entity.Player
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.Plugin
import org.koin.core.context.GlobalContext
import java.util.UUID

private const val PERK_DATA_METADATA_KEY = "perk_data"

class PerkData(
    internal val playerId: UUID,
    internal val enabledPerks: MutableList<String> = mutableListOf(),
    internal val boughtPerks: MutableList<String> = mutableListOf(),
) {

    var loadedFromDatasource = false

    fun mergeWithLoadedProjection(projection: PerkDataProjection) {
        check(playerId == projection.playerId) {
            "Cannot merge PerkDataProjection with the playerId '${projection.playerId}' with the PerkData with the playerId '$playerId'"
        }
        enabledPerks.addAll(projection.enabledPerks)
        boughtPerks.addAll(projection.boughtPerks)
        loadedFromDatasource = true
    }

}

fun Player.getOrCreatePerkData(): PerkData =
    getMetadata(PERK_DATA_METADATA_KEY).firstNotNullOfOrNull { it.value() as? PerkData } ?: let {
        val perkData = PerkData(playerId = uniqueId)
        setMetadata(PERK_DATA_METADATA_KEY, FixedMetadataValue(GlobalContext.get().get<Plugin>(), perkData))
        perkData
    }
