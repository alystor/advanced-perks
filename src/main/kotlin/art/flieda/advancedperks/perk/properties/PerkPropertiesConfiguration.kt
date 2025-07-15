package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.configuration.Configuration
import art.flieda.advancedperks.configuration.requireValue

class PerkPropertiesConfiguration(identifier: String) : Configuration("perks/$identifier.yml") {

    fun getProperties(): PerkProperties = getAny<PerkProperties>().requireValue()

    fun setProperty(property: PerkProperty, value: Any?) {
        check(property.mandatory.not() || value != null) {
            "Property '${property.key}' is mandatory and cannot be set to null."
        }

        val convertedValue = value?.let { objectMapper.convertValue(it, property.dataType.java) }

        setValue(property.key, convertedValue)
        saveConfiguration()
    }

}
