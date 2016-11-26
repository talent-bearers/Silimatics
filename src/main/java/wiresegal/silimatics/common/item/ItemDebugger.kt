package wiresegal.silimatics.common.item

import com.teamwizardry.librarianlib.client.util.TooltipHelper
import com.teamwizardry.librarianlib.common.base.item.IItemColorProvider
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.core.ModCreativeTab
import wiresegal.silimatics.common.lib.LibMisc
import java.awt.Color

class ItemDebugger(name: String) : ItemMod(name), IItemColorProvider {

    companion object {
        val shouldRegisterInDevEnv = false
    }

    init {
        ModCreativeTab.set(this)
    }

    val OCULATOR = LibMisc.MODID + ":oculator"
    override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, entity: Entity): Boolean {
        entity.entityData?.setTag(EntityPlayer.PERSISTED_NBT_TAG, NBTTagCompound())
        player.entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean(OCULATOR, true)
        return false
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i ->
            if (i == 0)
                Color.HSBtoRGB(Minecraft.getMinecraft().theWorld.totalWorldTime * 0.005f, 1f, 1f)
            else 0xFFFFFF
        }
    }

    override fun addInformation(stack: ItemStack?, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "smedry.lens.debugger.desc")
    }
}
