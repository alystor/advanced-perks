package art.flieda.advancedperks.guisystem.blueprint.representation

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface GuiRepresentationBlueprint {

    fun toGuiIcon(): ItemStack

}

fun defaultMissingRepresentation() = ItemStackRepresentationBlueprint(
    title = "Representation is missing",
    material = Material.BARRIER,
    amount = 14,
)

class GuiRepresentationBlueprintTypeAdapter : TypeAdapter<GuiRepresentationBlueprint>() {

    override fun write(writer: JsonWriter?, blueprint: GuiRepresentationBlueprint?) {
        TODO("Not yet needed")
    }

    override fun read(reader: JsonReader): GuiRepresentationBlueprint? {
        var title: String? = null
        var material: Material? = null
        var amount: Int? = null
        var textureValue: String? = null

        reader.beginObject()

        while (reader.hasNext()) {
            when (reader.nextName()) {
                "title" -> title = reader.nextString()
                "material" -> material = Material.valueOf(reader.nextString())
                "amount" -> amount = reader.nextInt()
                "textureValue" -> textureValue = reader.nextString()
                else -> reader.skipValue()
            }
        }

        reader.endObject()

        return when {
            title != null && material != null && amount != null ->
                ItemStackRepresentationBlueprint(
                    title = title,
                    material = material,
                    amount = amount,
                )

            title != null && textureValue != null && amount != null ->
                TexturedHeadRepresentationBlueprint(
                    title = title,
                    textureValue = textureValue,
                    amount = amount,
                )

            else -> throw IllegalArgumentException("Unable to determine representation type")
        }
    }

}
