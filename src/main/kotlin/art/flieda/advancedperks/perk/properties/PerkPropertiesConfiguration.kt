package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.core.configuration.Configuration

class PerkPropertiesConfiguration(identifier: String): Configuration("perks/$identifier.yml") {

    fun getProperties(): PerkProperties = PerkProperties(
        enabled = getBoolean("enabled").requireValue(),
        displayName = getString("display_name").requireValue(),
        description = getString("description").requireValue(),
        icon = getString("icon").requireValue(),
        permission = getString("permission"),
        price = getNumber("price")?.toDouble(),
        disallowedWorlds = getStringList("disallowed_worlds").requireValue() //TODO check correct yaml handling here
    )

}
