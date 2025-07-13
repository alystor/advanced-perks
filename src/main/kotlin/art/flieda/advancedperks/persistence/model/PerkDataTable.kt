package art.flieda.advancedperks.persistence.model

import org.jetbrains.exposed.v1.core.Table

object PerkDataTable : Table("perk_data") {
    val playerId = uuid("player_id")
    val enabledPerks = text("enabled_perks").default("[]")
    val boughtPerks = text("bought_perks").default("[]")

    override val primaryKey = PrimaryKey(playerId)
}