package wiresegal.silimatics.common.block

import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.init.Blocks
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.lib.LibNames
import wiresegal.silimatics.common.util.BrightsandPower
import wiresegal.zenmodelloader.common.block.base.BlockMod
import java.util.*

class BlockBrightsandDetector : BlockMod(LibNames.DETECTOR, Material.ROCK) {


    companion object {
        val POWERED = PropertyBool.create("powered")
    }

    init {
        defaultState = blockState.baseState.withProperty(POWERED, false)
        @Suppress("DEPRECATION")
        setHardness(Blocks.STONE.getBlockHardness(null, null, null))
    }

    override fun createBlockState(): BlockStateContainer?
        = BlockStateContainer(this, POWERED)

    override fun getMetaFromState(state: IBlockState): Int
        = if (state.getValue(POWERED)) 1 else 0

    override fun getStateFromMeta(meta: Int): IBlockState?
        = defaultState.withProperty(POWERED, meta == 1)

    override fun getWeakPower(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Int
        = if (blockState.getValue(POWERED)) 15 else 0

    override fun canProvidePower(state: IBlockState): Boolean = true

    override fun updateTick(worldObj: World, pos: BlockPos, bs: IBlockState, rand: Random?) {
        val state = worldObj.getBlockState(pos).getValue(POWERED)
        val entitiesAround = worldObj.getEntitiesWithinAABB(EntityFallingBlock::class.java, AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 0, 2)))
        var flag: Boolean = false
        entitiesAround.filter { it.block?.block == ModBlocks.sand && it.block?.getValue(BlockSand.SAND_TYPE) == EnumSandType.BRIGHT }.forEach { flag = true }
        val expectedState = BrightsandPower.hasBrightsandPower(worldObj, pos) || flag
        if(state != expectedState && !worldObj.isRemote)
            worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(POWERED, expectedState), 1 or 2)
        worldObj.scheduleUpdate(pos, this, 0)
    }

    override fun requiresUpdates(): Boolean {
        return true
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos?, state: IBlockState?) {
        worldIn.scheduleUpdate(pos, this, 0)
    }


}


