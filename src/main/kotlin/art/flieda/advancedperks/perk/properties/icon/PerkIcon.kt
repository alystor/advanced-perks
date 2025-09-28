package art.flieda.advancedperks.perk.properties.icon

import art.flieda.advancedperks.perk.properties.PerkProperties
import art.flieda.advancedperks.utilities.StringProcessingExtensions.applyReplacements
import art.flieda.advancedperks.utilities.itembuilder.ItemStackBuilder
import art.flieda.advancedperks.utilities.itembuilder.TexturedSkullBuilder
import com.cryptomorin.xseries.XMaterial
import org.bukkit.Material

private const val TEXTURE_PREFIX =
    "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv"

class PerkIcon(
    perkProperties: PerkProperties,
) {

    private val invalidIconDataIcon = ItemStackBuilder(Material.BARRIER)
        .setDisplayName("Invalid icon data specified.")
        .setDescription(listOf("Icon Data: ${perkProperties.icon}"))
        .build()

    val icon = run {
        val builder = if (perkProperties.icon.startsWith(TEXTURE_PREFIX)) {
            TexturedSkullBuilder().setTextureValue(perkProperties.icon)
        } else {
            val material = XMaterial.matchXMaterial(perkProperties.icon).get().get()
            if (material != null) {
                ItemStackBuilder(material)
            } else {
                return@run invalidIconDataIcon
            }
        }

        //TODO implement intelligent line splitting
        val descriptionLines = listOf(perkProperties.description)

        builder
            .setDisplayName(perkProperties.displayName.applyReplacements())
            .setDescription(descriptionLines.map { it.applyReplacements() })
            .build()
    }

}
