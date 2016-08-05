package wiresegal.silimatics.common.compat.waila

import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import mcp.mobius.waila.api.IWailaDataProvider
import mcp.mobius.waila.api.IWailaRegistrar
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import wiresegal.silimatics.common.block.BlockGlass
import wiresegal.silimatics.common.block.BlockGlassPane
import wiresegal.silimatics.common.core.ModBlocks

object WailaCompat {
    @JvmStatic
    fun onWailaCall(registrar: IWailaRegistrar) {
        println("smedry | Waila compat")
        registrar.registerStackProvider(PaneStackProvider(), BlockGlassPane::class.java)
        registrar.registerStackProvider(GlassStackProvider(), BlockGlass::class.java)

    }
}

class PaneStackProvider : IWailaDataProvider {
    override fun getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler?): ItemStack? {
        return ItemStack(ModBlocks.glassPane, 1, accessor.metadata)
    }

    override fun getWailaTail(itemStack: ItemStack?, currenttip: MutableList<String>?, accessor: IWailaDataAccessor?, config: IWailaConfigHandler?): MutableList<String>? {
        return null
    }

    override fun getNBTData(player: EntityPlayerMP?, te: TileEntity?, tag: NBTTagCompound?, world: World?, pos: BlockPos?): NBTTagCompound? {
        return null
    }

    override fun getWailaHead(itemStack: ItemStack?, currenttip: MutableList<String>?, accessor: IWailaDataAccessor?, config: IWailaConfigHandler?): MutableList<String>? {
        return null
    }

    override fun getWailaBody(itemStack: ItemStack?, currenttip: MutableList<String>?, accessor: IWailaDataAccessor?, config: IWailaConfigHandler?): MutableList<String>? {
        return null
    }

}

class GlassStackProvider : IWailaDataProvider {
    override fun getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler?): ItemStack? {
        return ItemStack(ModBlocks.glass, 1, accessor.metadata)
    }

    override fun getWailaTail(itemStack: ItemStack?, currenttip: MutableList<String>?, accessor: IWailaDataAccessor?, config: IWailaConfigHandler?): MutableList<String>? {
        return null
    }

    override fun getNBTData(player: EntityPlayerMP?, te: TileEntity?, tag: NBTTagCompound?, world: World?, pos: BlockPos?): NBTTagCompound? {
        return null
    }

    override fun getWailaHead(itemStack: ItemStack?, currenttip: MutableList<String>?, accessor: IWailaDataAccessor?, config: IWailaConfigHandler?): MutableList<String>? {
        return null
    }

    override fun getWailaBody(itemStack: ItemStack?, currenttip: MutableList<String>?, accessor: IWailaDataAccessor?, config: IWailaConfigHandler?): MutableList<String>? {
        return null
    }

}
