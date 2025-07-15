package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.configuration.Configuration
import art.flieda.advancedperks.configuration.requireValue

class PerkPropertiesConfiguration(identifier: String) : Configuration("perks/$identifier.yml") {

    fun getProperties(): PerkProperties = getAny<PerkProperties>().requireValue()

    fun setProperty(property: PerkProperty, value: Any?) {
        setValue(property.key, value)
        saveConfiguration()
    }

}
