package wiresegal.silimatics.common.item

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.ActionResult
import net.minecraft.util.EnumHand
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper
import wiresegal.zenmodelloader.common.ZenModelLoader
import wiresegal.zenmodelloader.common.core.IItemColorProvider

/**
 * @author WireSegal
 * Created at 8:34 AM on 8/4/16.
 */
class ItemLensFrames(name: String, armorMaterial: ArmorMaterial, vararg variants: String) : ItemModArmor(name, armorMaterial, EntityEquipmentSlot.HEAD, *variants), IItemColorProvider {

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

    init {
        maxDamage = 0
    }

    override fun getItemStackDisplayName(stack: ItemStack): String {
        val name = super.getItemStackDisplayName(stack)
        val lensStack = stack.getLensStack() ?: return name
        val lensName = ZenModelLoader.PROXY.translate(lensStack.unlocalizedName + ".plural.name")
        return "$name ${TextFormatting.WHITE}(${TextFormatting.GREEN}$lensName${TextFormatting.WHITE})"
    }

    override fun addInformation(stack: ItemStack?, playerIn: EntityPlayer?, tooltip: MutableList<String>?, advanced: Boolean) {
        if (stack?.getLensStack() != null && stack?.getLensStack()?.item is ItemLens)
            ItemLens.getLensFromStack(stack).addTooltip(stack, playerIn, tooltip, advanced)
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for(i in ItemLens.lenses.indices)
            subItems.add(ItemStack(itemIn).setLensStack(ItemStack(ModItems.lens, 1, i)))
    }

    override fun getArmorTexture(stack: ItemStack, entity: Entity?, slot: EntityEquipmentSlot?, type: String?): String {
        val lensStack = stack.getLensStack()
        val lensItem = lensStack?.item
        var second = ""
        if (lensStack != null && lensItem != null && lensItem == ModItems.lens) {
            if (lensStack.itemDamage == EnumSandType.DULL.ordinal) second = "Glass"
            else if (lensStack.itemDamage == EnumSandType.RASHID.ordinal) second = "Rashid"
        }
        return "${LibMisc.MODID}:textures/models/lensFrames${if (type == "overlay") "Overlay" else second}.png"
    }
    override fun onArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
        val lens = itemStack.getLensStack() ?: return

        if (lens.item is ILens)
            (lens.item as ILens).onUsingTick(world, player, itemStack)
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i ->
            if (i == 1) {
                val lensStack = itemStack.getLensStack()
                if (lensStack == null) 0xFFFFFF else Minecraft.getMinecraft().itemColors.getColorFromItemstack(lensStack, i)
            } else 0xFFFFFF
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getColor(stack: ItemStack): Int {
        val lensStack = stack.getLensStack()
        return if (lensStack == null)
            0xFEFEFE
        else
            Minecraft.getMinecraft().itemColors.getColorFromItemstack(lensStack, 1)
    }

    override fun hasColor(stack: ItemStack?): Boolean {
        return true
    }
}
