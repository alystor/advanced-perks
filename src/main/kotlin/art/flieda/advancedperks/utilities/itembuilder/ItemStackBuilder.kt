package art.flieda.advancedperks.utilities.itembuilder

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

open class ItemStackBuilder(
    material: Material,
) {

    private val itemStack: ItemStack = ItemStack(material)
    protected val meta: ItemMeta = itemStack.itemMeta!!

    fun setDisplayName(displayName: String): ItemStackBuilder = apply {
        meta.setDisplayName(displayName)
    }

    fun setDescription(description: List<String>): ItemStackBuilder = apply {
        meta.lore = description
    }

    fun setAmount(amount: Int): ItemStackBuilder = apply {
        itemStack.amount = amount
    }

    fun build(): ItemStack = itemStack.apply {
        this.itemMeta = meta
    }

}
