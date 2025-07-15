package art.flieda.advancedperks.perk.properties

//TODO rework this
data class PerkProperties(
    val enabled: Boolean,
    val displayName: String,
    val description: String,
    //TODO custom object for this
    val icon: String,
    val permission: String?,
    val price: Double?,
    val disallowedWorlds: List<String> = mutableListOf(),
)
