package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.AdvancedPerks.Companion.logger
import art.flieda.advancedperks.configuration.Configuration
import art.flieda.advancedperks.configuration.requireValue

class PerkPropertiesConfiguration(identifier: String) : Configuration("perks/$identifier.yml") {

    fun getProperties(): PerkProperties = getAny<PerkProperties>().requireValue()

    fun setProperty(property: PerkProperty, value: Any?): PerkSetPropertyResult {
        val convertedValue = runCatching {
            value?.let { objectMapper.convertValue(it, property.dataType.java) }
        }

        return when {
            property.mandatory && value == null -> {
                logger.warning { "Property '${property.configurationKey}' is mandatory and cannot be set to null." }
                PerkSetPropertyResult.NULL_ON_MANDATORY_PROPERTY
            }

            convertedValue.isFailure -> {
                logger.warning { "The input value for '${property.configurationKey}' has thrown an error during its conversion process: ${convertedValue.exceptionOrNull()?.message}" }
                PerkSetPropertyResult.INPUT_CONVERSION_FAILED
            }

            property.mandatory && convertedValue.getOrNull() == null -> {
                logger.warning { "Property '${property.configurationKey}' is mandatory and its input value has lead to a null value." }
                PerkSetPropertyResult.NULL_ON_MANDATORY_PROPERTY
            }

            else -> {
                setValue(property.configurationKey, convertedValue)
                saveConfiguration()
                PerkSetPropertyResult.SUCCESS
            }
        }
    }

}
