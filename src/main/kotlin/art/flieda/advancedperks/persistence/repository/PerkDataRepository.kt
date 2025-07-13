package art.flieda.advancedperks.persistence.repository

import art.flieda.advancedperks.persistence.model.PerkDataTable
import org.jetbrains.exposed.v1.jdbc.select
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.util.UUID

class PerkDataRepository {

    fun findPerkDataById(playerId: UUID) = transaction {
        PerkDataTable
            .select(PerkDataTable.perkIdentifier, PerkDataTable.state)
            .where { PerkDataTable.playerId eq playerId }
            .toProjection()
    }

}