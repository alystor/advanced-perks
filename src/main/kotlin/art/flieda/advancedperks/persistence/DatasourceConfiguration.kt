package art.flieda.advancedperks.persistence

import art.flieda.advancedperks.configuration.Configuration
import art.flieda.advancedperks.configuration.orDefault
import art.flieda.advancedperks.configuration.requireValue

class DatasourceConfiguration : Configuration("datasource.yml") {

    //TODO maybe directly do getAny<DatasourceType>
    val datasourceType = getAny<DatasourceType>("datasource.type").orDefault(DatasourceType.FILE)

    val fileLocation = getAny<String>("datasource.file.file_location").orDefault("data.db")

    val databaseUrl = getAny<String>("datasource.database.url").requireValue()

    val databaseUsername = getAny<String>("datasource.database.credentials.username").requireValue()

    val databasePassword = getAny<String>("datasource.database.credentials.password").requireValue()

}
