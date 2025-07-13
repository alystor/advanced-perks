package art.flieda.advancedperks.perkdata.repository

import art.flieda.advancedperks.perkdata.PerkData
import art.flieda.advancedperks.perkdata.model.PerkDataTable
import art.flieda.advancedperks.perkdata.model.PerkState
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.jdbc.batchInsert
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.util.UUID

class PerkDataRepository {

    fun findPerkDataById(playerId: UUID) = transaction {
        PerkDataTable
            .selectAll()
            .where { PerkDataTable.playerId eq playerId }
            .toProjection()
    }

    fun savePerkData(perkData: PerkData) = transaction {
        PerkDataTable.deleteWhere {
            playerId eq perkData.playerId
        }

        PerkDataTable.batchInsert(perkData.enabledPerks) { data ->
            this[PerkDataTable.playerId] = perkData.playerId
            this[PerkDataTable.perkIdentifier] = data
            this[PerkDataTable.state] = PerkState.ENABLED
        }

        PerkDataTable.batchInsert(perkData.boughtPerks) { data ->
            this[PerkDataTable.playerId] = perkData.playerId
            this[PerkDataTable.perkIdentifier] = data
            this[PerkDataTable.state] = PerkState.BOUGHT
        }
    }
}
