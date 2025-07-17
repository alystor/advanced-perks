package art.flieda.advancedperks.perk.properties

import kotlin.reflect.KClass

enum class PerkProperty(
    val configurationKey: String,
    val mandatory: Boolean,
    val dataType: KClass<*>,
    val exampleValue: String = "",
    val isList: Boolean = false
) {
    ENABLED("enabled", true, Boolean::class, "true or false"),
    DISPLAY_NAME("display_name", true, String::class, "any text"),
    DESCRIPTION("description", true, String::class, "any text"),
    ICON("icon", true, String::class, "any text"),
    PERMISSION("permission", false, String::class, "any text"),
    PRICE("price", false, Double::class, "any number >= 0"),
    DISALLOWED_WORLDS("disallowed_worlds", false, String::class, "[ \"value1\", \"value2\", \"valueN\" ]", true);

    companion object {

        fun findPerkPropertyByConfigurationKey(configurationKey: String): PerkProperty? =
            PerkProperty.entries.find { property ->
                property.configurationKey.equals(
                    configurationKey,
                    ignoreCase = true
                )
            }

    }
}
