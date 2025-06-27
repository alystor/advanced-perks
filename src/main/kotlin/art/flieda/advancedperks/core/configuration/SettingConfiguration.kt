package art.flieda.advancedperks.core.configuration

class SettingConfiguration : Configuration("setting.yml") {

    fun getDefaultMaxActivePerks(): Int = getNumber("perk.default_max_active_perks", 5).toInt()

}