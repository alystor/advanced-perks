package art.flieda.advancedperks.configuration

private const val DEFAULT_MAX_ACTIVE_PERKS = 5

class SettingConfiguration : Configuration("setting.yml") {

    fun getDefaultMaxActivePerks(): Int = getAny<Int>("perk.default_max_active_perks")
        .orDefault(DEFAULT_MAX_ACTIVE_PERKS)

}
