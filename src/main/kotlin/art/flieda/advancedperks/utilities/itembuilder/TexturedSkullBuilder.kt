package art.flieda.advancedperks.utilities.itembuilder

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.meta.SkullMeta
import java.net.URI
import java.net.URL
import java.util.Base64
import java.util.UUID

class TexturedSkullBuilder : ItemStackBuilder(
    Material.PLAYER_HEAD,
) {

    fun setTextureValue(textureValue: String): TexturedSkullBuilder = apply {
        val profile = Bukkit.createPlayerProfile(UUID.randomUUID()).apply {
            setTextures(
                textures.apply {
                    skin = getUrlFromBase64(textureValue)
                }
            )
        }
        (meta as SkullMeta).ownerProfile = profile
    }

    // TODO test this
    private fun getUrlFromBase64(base64: String): URL {
        val decoded = String(Base64.getDecoder().decode(base64))
        val url = Regex(""""url":"([^"]+)"""").find(decoded)?.groupValues?.get(1)
            ?: throw IllegalArgumentException("Invalid skin texture format")
        return URI.create(url).toURL()
    }

}
