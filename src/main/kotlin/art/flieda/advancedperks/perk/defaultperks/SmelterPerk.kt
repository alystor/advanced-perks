package art.flieda.advancedperks.perk.defaultperks

import art.flieda.advancedperks.perk.AbstractPerk
import art.flieda.advancedperks.perk.types.ListenerPerk
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockDropItemEvent

class SmelterPerk : AbstractPerk("smelter"), ListenerPerk {

    @EventHandler
    fun onBlockDropItem(event: BlockDropItemEvent) {
        event.player.runWhenPerkIsEnabled {
            event.items.forEach { item ->
                item.itemStack.type = convertToProcessedForm(item.itemStack.type)
            }
        }
    }

    private fun convertToProcessedForm(material: Material): Material =
        when (material) {
            Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE -> Material.COAL
            Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.RAW_IRON -> Material.IRON_INGOT
            Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.RAW_GOLD -> Material.GOLD_INGOT
            Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, Material.RAW_COPPER -> Material.COPPER_INGOT
            Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE -> Material.DIAMOND
            Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE -> Material.EMERALD
            Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE -> Material.LAPIS_LAZULI
            Material.NETHER_QUARTZ_ORE -> Material.QUARTZ
            Material.REDSTONE_ORE, Material.DEEPSLATE_REDSTONE_ORE -> Material.REDSTONE
            Material.NETHER_GOLD_ORE -> Material.GOLD_NUGGET
            Material.ANCIENT_DEBRIS -> Material.NETHERITE_SCRAP
            else -> material
        }

}
