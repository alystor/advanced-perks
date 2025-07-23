package art.flieda.advancedperks.updatechecker

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@JsonIgnoreProperties(ignoreUnknown = true)
data class UpdateData(
    val name: String,
    @JsonProperty("releaseDate")
    private val releaseDate: Long,
) {

    val releaseDateFormatted: String
        get() = DateTimeFormatter.ofPattern("dd.MM.yyyy").let { formatter ->
            Instant.ofEpochMilli(releaseDate)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
                .format(formatter)
        }

}
