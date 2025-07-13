package art.flieda.advancedperks.persistence

import art.flieda.advancedperks.perkdata.model.PerkDataTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.qualifier.named
import java.io.File

class DatasourceManager(
    configuration: DatasourceConfiguration,
) : KoinComponent {

    val pluginDirectory: File = get<File>(named("pluginDirectory"))

    init {
        when (configuration.datasourceType) {
            DatasourceType.FILE -> {
                val databaseFile = File(pluginDirectory, configuration.fileLocation)
                databaseFile.takeUnless { file -> file.exists() }?.createNewFile()
                Database.connect(url = "jdbc:sqlite:${databaseFile.path}", driver = "org.sqlite.JDBC")
            }

            DatasourceType.DATABASE -> {
                Database.connect(
                    url = configuration.databaseUrl,
                    user = configuration.databaseUsername,
                    password = configuration.databasePassword
                )
            }
        }

        transaction {
            SchemaUtils.create(PerkDataTable)
        }
    }

}