package art.flieda.advancedperks.perkdata.model

import org.jetbrains.exposed.v1.core.Table

object PerkDataTable : Table("perk_data") {
    val playerId = uuid("player_id")
    val perkIdentifier = text("perk_identifier")
    val state = enumeration("state", PerkState::class)

    override val primaryKey = PrimaryKey(playerId, perkIdentifier, state)
}
