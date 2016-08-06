package wiresegal.silimatics.common.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemArmor
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.Loader
import wiresegal.silimatics.common.core.ModCreativeTab
import wiresegal.zenmodelloader.common.core.IVariantHolder
import wiresegal.zenmodelloader.common.core.VariantHelper

/**
 * @author WireSegal
 * Created at 8:31 AM on 8/4/16.
 */
open class ItemModArmor(name: String, armorMaterial: ArmorMaterial, entityEquipmentSlot: EntityEquipmentSlot, vararg variants: String) : ItemArmor(armorMaterial, 0, entityEquipmentSlot), IVariantHolder {

    override val variants: Array<out String>

    private val bareName: String
    private val modId: String

    init {
        modId = Loader.instance().activeModContainer().modId
        bareName = name
        this.variants = VariantHelper.setupItem(this, name, variants)
        ModCreativeTab.set(this)
    }

    override fun setUnlocalizedName(name: String): Item {
        VariantHelper.setUnlocalizedNameForItem(this, modId, name)
        return super.setUnlocalizedName(name)
    }

    override fun getUnlocalizedName(stack: ItemStack): String {
        val dmg = stack.itemDamage
        val variants = this.variants
        val name: String
        if (dmg >= variants.size) {
            name = this.bareName
        } else {
            name = variants[dmg]
        }

        return "item.$modId:$name"
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for (i in 0..this.variants.size - 1) {
            subItems.add(ItemStack(itemIn, 1, i))
        }
    }
}
