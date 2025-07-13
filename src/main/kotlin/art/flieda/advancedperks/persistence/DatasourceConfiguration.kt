package art.flieda.advancedperks.persistence

import art.flieda.advancedperks.core.configuration.Configuration

class DatasourceConfiguration : Configuration("datasource.yml") {

    val datasourceType = DatasourceType.valueOfIgnoreCase(getString("datasource.type", "file").requireValue())

    val fileLocation = getString("datasource.file.file_location", "data.db").requireValue()

    val databaseUrl = getString("datasource.database.url").requireValue()

    val databaseUsername = getString("datasource.database.credentials.username").requireValue()

    val databasePassword = getString("datasource.database.credentials.password").requireValue()

}
