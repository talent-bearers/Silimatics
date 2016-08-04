package wiresegal.silimatics.common.item

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.core.ModItems
import wiresegal.zenmodelloader.common.lib.LibMisc

/**
 * @author WireSegal
 * Created at 8:34 AM on 8/4/16.
 */
class ItemLensFrames(name: String, armorMaterial: ArmorMaterial, vararg variants: String) : ItemModArmor(name, armorMaterial, EntityEquipmentSlot.HEAD, *variants) {

    companion object {
        val TAG_LENS = "lens"

        fun ItemStack.getLensStack(): ItemStack? {
            val compound = ItemNBTHelper.getCompound(this, TAG_LENS, true) ?: return null
            return ItemStack.loadItemStackFromNBT(compound)
        }

        fun ItemStack.setLensStack(lens: ItemStack): ItemStack {
            ItemNBTHelper.setCompound(this, TAG_LENS, lens.writeToNBT(NBTTagCompound()))
            return this
        }
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World?, playerIn: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack>? {
        itemStackIn.setLensStack(ItemStack(ModItems.lens))
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)
    }

    override fun addInformation(stack: ItemStack?, playerIn: EntityPlayer?, tooltip: MutableList<String>?, advanced: Boolean) {
        if (stack?.getLensStack() != null && stack?.getLensStack()?.item is ItemLens)
            ItemLens.getLensFromStack(stack).addTooltip(stack, playerIn, tooltip, advanced)
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        subItems.add(ItemStack(itemIn))
        for(i in ItemLens.lenses.indices)
            subItems.add(ItemStack(itemIn).setLensStack(ItemStack(ModItems.lens, 1, i)))
    }

    override fun getArmorTexture(stack: ItemStack?, entity: Entity?, slot: EntityEquipmentSlot?, type: String?): String {
        return "${LibMisc.MOD_ID}:textures/models/lensFrames${if (type == "overlay") "Overlay" else ""}.png"
    }
    override fun onArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
        val lens = itemStack.getLensStack() ?: return

        if (lens.item is ILens)
            (lens.item as ILens).onUsingTick(world, player, itemStack)
    }
}
