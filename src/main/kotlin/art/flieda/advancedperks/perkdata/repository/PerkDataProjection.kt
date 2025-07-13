package art.flieda.advancedperks.perkdata.repository

import art.flieda.advancedperks.perkdata.model.PerkDataTable
import art.flieda.advancedperks.perkdata.model.PerkState
import org.jetbrains.exposed.v1.jdbc.Query
import java.util.UUID

data class PerkDataProjection(
    val playerId: UUID,
    val enabledPerks: List<String>,
    val boughtPerks: List<String>,
)

fun Query.toProjection(): PerkDataProjection? = firstOrNull()?.let { playerId ->
    PerkDataProjection(
        playerId = playerId[PerkDataTable.playerId],
        enabledPerks = filter { row -> row[PerkDataTable.state] == PerkState.ENABLED }.map { row -> row[PerkDataTable.perkIdentifier] },
        boughtPerks = filter { row -> row[PerkDataTable.state] == PerkState.BOUGHT }.map { row -> row[PerkDataTable.perkIdentifier] }
    )
}
