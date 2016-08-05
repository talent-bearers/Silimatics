package wiresegal.silimatics.common.item

import com.google.common.collect.Multimap
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.Entity
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.common.ZenModelLoader
import wiresegal.zenmodelloader.common.core.IItemColorProvider

/**
 * @author WireSegal
 * Created at 8:34 AM on 8/4/16.
 */
class ItemLensFrames(name: String, armorMaterial: ArmorMaterial, vararg variants: String) : ItemModArmor(name, armorMaterial, EntityEquipmentSlot.HEAD, *variants), IItemColorProvider {

    companion object {
        val TAG_LENS = "lens"

        fun ItemStack.getLensStack(): ItemStack {
            val compound = ItemNBTHelper.getCompound(this, TAG_LENS, true)

            if (compound == null) {
                this.setLensStack(null)
                return ItemStack(ModItems.lens)
            }

            val stack = ItemStack.loadItemStackFromNBT(compound)

            if (stack.item !is ILens) {
                this.setLensStack(null)
                return ItemStack(ModItems.lens)
            }

            return stack
        }

        fun ItemStack.setLensStack(lens: ItemStack?): ItemStack {
            ItemNBTHelper.setCompound(this, TAG_LENS, (lens ?: ItemStack(ModItems.lens)).writeToNBT(NBTTagCompound()))
            return this
        }
    }

    init {
        maxDamage = 0
    }

    override fun getItemStackDisplayName(stack: ItemStack): String {
        val name = super.getItemStackDisplayName(stack)
        val lensStack = stack.getLensStack()
        val lensName = ZenModelLoader.PROXY.translate(lensStack.unlocalizedName + ".plural.name")
        return "$name ${TextFormatting.WHITE}(${TextFormatting.GREEN}$lensName${TextFormatting.WHITE})"
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        val lensStack = stack.getLensStack()
        (lensStack.item as ILens).addTooltip(lensStack, playerIn, tooltip, advanced)
    }

    override fun getSubItems(itemIn: Item, tab: CreativeTabs?, subItems: MutableList<ItemStack>) {
        for(i in ItemLens.lenses.indices)
            subItems.add(ItemStack(itemIn).setLensStack(ItemStack(ModItems.lens, 1, i)))
    }

    override fun getArmorTexture(stack: ItemStack, entity: Entity?, slot: EntityEquipmentSlot?, type: String?): String {
        val lensStack = stack.getLensStack()
        val lensItem = lensStack.item
        var second = ""
        if (lensItem == ModItems.lens) {
            if (lensStack.itemDamage == EnumSandType.DULL.ordinal) second = "Glass"
            else if (lensStack.itemDamage == EnumSandType.RASHID.ordinal) second = "Rashid"
        }
        return "${LibMisc.MODID}:textures/models/lensFrames${if (type == "overlay") "Overlay" else second}.png"
    }

    override fun onArmorTick(world: World, player: EntityPlayer, itemStack: ItemStack) {
        val lens = itemStack.getLensStack()
        (lens.item as ILens).onUsingTick(world, player, lens)
        itemStack.setLensStack(lens)
    }

    override fun getAttributeModifiers(slot: EntityEquipmentSlot, stack: ItemStack): Multimap<String, AttributeModifier> {
        val multimap = super.getAttributeModifiers(slot, stack)
        val lensStack = stack.getLensStack()
        val item = (lensStack.item as ILens)
        item.addAttributes(slot, lensStack, multimap)
        return multimap
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i ->
            if (i == 1) {
                val lensStack = itemStack.getLensStack()
                Minecraft.getMinecraft().itemColors.getColorFromItemstack(lensStack, i)
            } else 0xFFFFFF
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getColor(stack: ItemStack): Int {
        val lensStack = stack.getLensStack()
        return Minecraft.getMinecraft().itemColors.getColorFromItemstack(lensStack, 1)
    }

    override fun hasColor(stack: ItemStack?): Boolean {
        return true
    }
}
