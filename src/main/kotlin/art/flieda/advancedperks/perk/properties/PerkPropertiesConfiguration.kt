package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.AdvancedPerks.Companion.logger
import art.flieda.advancedperks.configuration.Configuration
import art.flieda.advancedperks.configuration.requireValue

class PerkPropertiesConfiguration(identifier: String) : Configuration("perks/$identifier.yml") {

    fun getProperties(): PerkProperties = getAny<PerkProperties>().requireValue()

    fun deleteProperty(property: PerkProperty): DeletePropertyResult = when {
        property.modifiable.not() -> DeletePropertyResult.PROPERTY_NOT_MODIFIABLE

        property.mandatory -> {
            logger.warning { "The value of property '${property.configurationKey}' cannot be removed because its a mandatory property." }
            DeletePropertyResult.PROPERTY_IS_MANDATORY
        }

        else -> {
            setValue(property.configurationKey, null)
            saveConfiguration()
            DeletePropertyResult.SUCCESS
        }
    }

    fun setProperty(property: PerkProperty, value: Any): SetPropertyResult {
        if (property.modifiable.not()) return SetPropertyResult.PROPERTY_NOT_MODIFIABLE

        val convertedValueResult = runCatching {
            objectMapper.convertValue(value.prepareValueForConversion(), property.dataType.java)
        }

        return when {
            convertedValueResult.isFailure -> {
                logger.warning { "The input value for '${property.configurationKey}' has thrown an error during its conversion process: ${convertedValueResult.exceptionOrNull()?.message}" }
                SetPropertyResult.INPUT_CONVERSION_FAILED
            }

            else -> {
                setValue(property.configurationKey, convertedValueResult.getOrThrow())
                saveConfiguration()
                SetPropertyResult.SUCCESS
            }
        }
    }

    private fun Any.prepareValueForConversion(): Any = when {
        this is String -> {
            if (this.equals("null", true)) this else runCatching { objectMapper.readTree(this) }.getOrElse { this }
        }

        else -> this
    }
}
