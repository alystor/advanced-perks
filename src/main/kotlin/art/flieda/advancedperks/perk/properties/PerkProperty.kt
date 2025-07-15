package art.flieda.advancedperks.perk.properties

import kotlin.reflect.KClass

enum class PerkProperty(val key: String, val mandatory: Boolean, val dataType: KClass<*>) {
    ENABLED("enabled", true, Boolean::class),
    DISPLAY_NAME("display_name", true, String::class),
    DESCRIPTION("description", true, String::class),
    ICON("icon", true, String::class),
    PERMISSION("permission", false, String::class),
    PRICE("price", false, Double::class),
    DISALLOWED_WORLDS("disallowed_worlds", false, List::class)
}
