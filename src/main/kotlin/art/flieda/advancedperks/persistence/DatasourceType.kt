package art.flieda.advancedperks.persistence

enum class DatasourceType {

    FILE,
    DATABASE;

    companion object {
        fun valueOfIgnoreCase(name: String): DatasourceType {
            return entries.firstOrNull { it.name.equals(name, ignoreCase = true) }
                ?: throw IllegalArgumentException("No enum constant matches the name: $name")
        }
    }

}
