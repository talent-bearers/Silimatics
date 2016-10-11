package wiresegal.silimatics.common.block

import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.silimatics.common.lib.LibNames
import wiresegal.silimatics.common.util.BrightsandPower
import wiresegal.zenmodelloader.common.block.base.BlockMod

class BlockBrightsandDetector : BlockMod(LibNames.DETECTOR, Material.ROCK), ITileEntityProvider {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity?
        = TileDetector()


    companion object {
        val POWERED = PropertyBool.create("powered")
    }

    init {
        defaultState = blockState.baseState.withProperty(POWERED, false)
        GameRegistry.registerTileEntity(TileDetector::class.java, "brightSandDetector")
    }

    override fun createBlockState(): BlockStateContainer?
        = BlockStateContainer(this, POWERED)

    override fun getMetaFromState(state: IBlockState): Int
        = if (state.getValue(POWERED)) 1 else 0

    override fun getStateFromMeta(meta: Int): IBlockState?
        = defaultState.withProperty(POWERED, meta == 1)

    override fun hasTileEntity(state: IBlockState?): Boolean
        = true

    override fun getWeakPower(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Int
        = if (blockState.getValue(POWERED)) 15 else 0

    override fun canProvidePower(state: IBlockState): Boolean = true

    class TileDetector : TileEntity(), ITickable {
        override fun update() {
            val state = worldObj.getBlockState(getPos()).getValue(POWERED)
            val expectedState = BrightsandPower.hasBrightsandPower(worldObj, pos)
            if(state != expectedState && !worldObj.isRemote)
                worldObj.setBlockState(getPos(), worldObj.getBlockState(getPos()).withProperty(POWERED, expectedState), 1 or 2);
        }
    }
}


