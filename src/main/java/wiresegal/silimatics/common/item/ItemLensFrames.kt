package wiresegal.silimatics.common.item

import com.google.common.collect.Lists
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.client.renderer.color.ItemColors
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper
import wiresegal.zenmodelloader.common.core.IItemColorProvider

/**
 * @author WireSegal
 * Created at 8:34 AM on 8/4/16.
 */
class ItemLensFrames(name: String, armorMaterial: ArmorMaterial, vararg variants: String) : ItemModArmor(name, armorMaterial, EntityEquipmentSlot.HEAD, *variants), IItemColorProvider {

    companion object {
        val TAG_LENS = "lens"

        fun getLensStack(stack: ItemStack): ItemStack? {
            val compound = ItemNBTHelper.getCompound(stack, TAG_LENS, true) ?: return null
            return ItemStack.loadItemStackFromNBT(compound)
        }

        fun setLensStack(stack: ItemStack, lens: ItemStack) {
            ItemNBTHelper.setCompound(stack, TAG_LENS, lens.writeToNBT(NBTTagCompound()))
        }
    }

    init {
        maxDamage = 0
    }

    override fun onItemRightClick(itemStackIn: ItemStack, worldIn: World?, playerIn: EntityPlayer?, hand: EnumHand?): ActionResult<ItemStack>? {
        setLensStack(itemStackIn, ItemStack(ModItems.lens, 1, EnumSandType.STORM.ordinal)) //debug
        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand)
    }

    override fun getArmorTexture(stack: ItemStack?, entity: Entity?, slot: EntityEquipmentSlot?, type: String?): String {
        return "${LibMisc.MODID}:textures/models/lensFrames${if (type == "overlay") "Overlay" else ""}.png"
    }
    
    override fun onArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
        val lens = getLensStack(itemStack) ?: return

        if (lens.item is ILens)
            (lens.item as ILens).onUsingTick(world, player, itemStack)
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i ->
            if (i == 1) {
                val lensStack = getLensStack(itemStack)
                if (lensStack == null) 0xFFFFFF else Minecraft.getMinecraft().itemColors.getColorFromItemstack(lensStack, i)
            } else 0xFFFFFF
        }
    }

    @SideOnly(Side.CLIENT)
    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        val lensStack = getLensStack(stack) ?: return
        val tempTip = mutableListOf<String>()
        (lensStack.item ?: return).addInformation(lensStack, playerIn, tempTip, advanced)
        if (tempTip.size > 0)
            TooltipHelper.tooltipIfShift(tooltip) { tooltip.addAll(tempTip) }
    }

    override fun hasColor(stack: ItemStack?): Boolean {
        return true
    }

    @SideOnly(Side.CLIENT)
    override fun getColor(stack: ItemStack): Int {
        val lensStack = getLensStack(stack)
        return if (lensStack == null)
            0xFFFFFF
        else
            Minecraft.getMinecraft().itemColors.getColorFromItemstack(lensStack, 1)
    }
}
