package art.flieda.advancedperks.updatechecker

sealed class UpdateCheckerResult {
    data class UpdateAvailable(val version: String, val releaseDate: String) : UpdateCheckerResult()
    data class ErrorOnCheck(val errorMessage: String) : UpdateCheckerResult()
    object NoUpdateAvailable : UpdateCheckerResult()
    object TimeoutOnCheck : UpdateCheckerResult()
}
