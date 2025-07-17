package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.AdvancedPerks.Companion.logger
import art.flieda.advancedperks.configuration.Configuration
import art.flieda.advancedperks.configuration.requireValue

class PerkPropertiesConfiguration(identifier: String) : Configuration("perks/$identifier.yml") {

    fun getProperties(): PerkProperties = getAny<PerkProperties>().requireValue()

    fun setProperty(property: PerkProperty, value: Any): PerkSetPropertyResult {
        val convertedValueResult = runCatching {
            objectMapper.convertValue(value.prepareValueForConversion(), property.dataType.java)
        }

        return when {
            convertedValueResult.isFailure -> {
                logger.warning { "The input value for '${property.configurationKey}' has thrown an error during its conversion process: ${convertedValueResult.exceptionOrNull()?.message}" }
                PerkSetPropertyResult.INPUT_CONVERSION_FAILED
            }

            else -> {
                setValue(property.configurationKey, convertedValueResult.getOrThrow())
                saveConfiguration()
                PerkSetPropertyResult.SUCCESS
            }
        }
    }

    private fun Any.prepareValueForConversion(): Any =
        when {
            this is String -> runCatching { objectMapper.readTree(this) }.getOrElse { this }

            else -> this
        }
}
