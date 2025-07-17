package art.flieda.advancedperks.perk.properties

import art.flieda.advancedperks.AdvancedPerks.Companion.logger
import art.flieda.advancedperks.configuration.Configuration
import art.flieda.advancedperks.configuration.requireValue
import com.fasterxml.jackson.core.type.TypeReference
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

class PerkPropertiesConfiguration(identifier: String) : Configuration("perks/$identifier.yml") {

    fun getProperties(): PerkProperties = getAny<PerkProperties>().requireValue()

    fun setProperty(property: PerkProperty, value: Any?): PerkSetPropertyResult {
        val convertedValue = convertValueSafely<Any>(value, property)

        return when {
            property.mandatory && (value == null || value == "null") -> {
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
                setValue(property.configurationKey, convertedValue.getOrThrow()).also {
                    saveConfiguration()
                }
                PerkSetPropertyResult.SUCCESS
            }
        }
    }

    private fun <R> convertValueSafely(rawInput: Any?, property: PerkProperty): Result<R> {
        return runCatching {
            val targetJavaType: Type = if (property.isList) {
                createListType(property.dataType)
            } else {
                property.dataType.java
            }
            val jacksonJavaType = objectMapper.constructType(targetJavaType)

            val finalDataToConvert: Any? = when (rawInput) {
                is String -> {
                    objectMapper.readTree(rawInput)
                }

                else -> {
                    rawInput
                }
            }
            objectMapper.convertValue(finalDataToConvert, jacksonJavaType) as R // Cast to R
        }
    }

    fun createListType(elementType: KClass<*>): Type {
        return object : ParameterizedType {
            override fun getActualTypeArguments(): Array<Type> = arrayOf(elementType.java)
            override fun getRawType(): Type = List::class.java
            override fun getOwnerType(): Type? = null
        }
    }
}
