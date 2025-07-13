package art.flieda.advancedperks.configuration

private const val DEFAULT_MAX_ACTIVE_PERKS = 5

class SettingConfiguration : Configuration("setting.yml") {

    fun getDefaultMaxActivePerks(): Int = getNumber("perk.default_max_active_perks", DEFAULT_MAX_ACTIVE_PERKS).toInt()

}
