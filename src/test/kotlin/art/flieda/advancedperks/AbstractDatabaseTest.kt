package art.flieda.advancedperks

import art.flieda.advancedperks.core.configuration.SettingConfiguration
import art.flieda.advancedperks.persistence.DatasourceConfiguration
import art.flieda.advancedperks.persistence.DatasourceManager
import art.flieda.advancedperks.persistence.DatasourceType
import io.mockk.every
import io.mockk.mockk
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.jdbc.deleteAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import org.reflections.Reflections

private val dataSourceConfigurationMock = mockk<DatasourceConfiguration>().apply {
    every { datasourceType } returns DatasourceType.DATABASE
    every { databaseUrl } returns "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    every { databaseUsername } returns "sa"
    every { databasePassword } returns ""
}

abstract class AbstractDatabaseTest : AbstractTest(
    module {
        single { dataSourceConfigurationMock } bind DatasourceConfiguration::class
        singleOf(::DatasourceManager) { createdAtStart() }
        singleOf(::SettingConfiguration)
    }
) {

    @BeforeEach
    fun removeDatabaseEntries() {
        val tableClasses = Reflections("art.flieda.advancedperks")
            .getSubTypesOf(Table::class.java)

        transaction {
            tableClasses.mapNotNull { tableClass -> tableClass.kotlin.objectInstance }
                .forEach { table -> table.deleteAll() }
        }
    }

}
