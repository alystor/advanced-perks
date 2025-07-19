package art.flieda.advancedperks.updatechecker

data class SemanticVersion(
    val major: Int,
    val minor: Int,
    val patch: Int
) : Comparable<SemanticVersion> {

    override fun compareTo(other: SemanticVersion): Int {
        return when {
            major != other.major -> major - other.major
            minor != other.minor -> minor - other.minor
            else -> patch - other.patch
        }
    }

}

fun String.toSemanticVersion(): SemanticVersion = runCatching {
    this.substringBefore("-").split(".")
        .mapNotNull { it.toIntOrNull() }
        .filter { it >= 0 }
        .let {
            SemanticVersion(it[0], it[1], it[2])
        }
}.getOrNull() ?: throw IllegalArgumentException("Invalid semantic version format for version string: '$this'")
