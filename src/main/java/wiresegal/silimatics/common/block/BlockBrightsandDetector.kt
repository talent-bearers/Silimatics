package wiresegal.silimatics.common.block

import net.minecraft.block.BlockHorizontal
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.Mirror
import net.minecraft.util.Rotation
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

class BlockBrightsandDetector : BlockMod(LibNames.DETECTOR, Material.GLASS) {

    override fun getDrops(world: IBlockAccess?, pos: BlockPos?, state: IBlockState?, fortune: Int): MutableList<ItemStack>? {
        return mutableListOf(ItemStack(Item.getItemFromBlock(state!!.block)))
    }

    companion object {
        val POWERED = PropertyBool.create("powered")
    }

    init {
        defaultState = blockState.baseState.withProperty(POWERED, false)
        @Suppress("DEPRECATION")
        setHardness(Blocks.STONE.getBlockHardness(null, null, null))
    }

    override fun canRenderInLayer(state: IBlockState?, layer: BlockRenderLayer?): Boolean {
        return layer == BlockRenderLayer.CUTOUT
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isFullyOpaque(state: IBlockState?): Boolean {
        return false;
    }

    override fun isSideSolid(base_state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, side: EnumFacing?): Boolean {
        return false
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    override fun onBlockPlaced(worldIn: World, pos: BlockPos, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase): IBlockState {
        val placerFacing = placer.horizontalFacing
        return this.defaultState.withProperty(BlockHorizontal.FACING, placerFacing.opposite).withProperty(POWERED, false)
    }

    override fun isTranslucent(state: IBlockState?): Boolean {
        return true
    }

    override fun createBlockState(): BlockStateContainer?
        = BlockStateContainer(this, POWERED, BlockHorizontal.FACING)


    override fun getStateFromMeta(meta: Int): IBlockState {
        return this.defaultState.withProperty(POWERED, meta and 4 != 0).withProperty(BlockHorizontal.FACING, EnumFacing.getHorizontal(meta and 3))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        val facing = state.getValue<EnumFacing>(BlockHorizontal.FACING).horizontalIndex
        return if (state.getValue<Boolean>(POWERED)) facing or 4 else facing
    }

    override fun getWeakPower(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Int
        = if (blockState.getValue(POWERED) && blockState.getValue(BlockHorizontal.FACING) == side.opposite) 15 else 0

    override fun withRotation(state: IBlockState, rot: Rotation): IBlockState {
        return state.withProperty(BlockHorizontal.FACING, rot.rotate(state.getValue(BlockHorizontal.FACING)))
    }

    override fun withMirror(state: IBlockState, mirrorIn: Mirror): IBlockState {
        return state.withRotation(mirrorIn.toRotation(state.getValue(BlockHorizontal.FACING)))
    }

    override fun canProvidePower(state: IBlockState): Boolean = true

    override fun canConnectRedstone(state: IBlockState, world: IBlockAccess?, pos: BlockPos?, side: EnumFacing): Boolean {
        return state.getValue(BlockHorizontal.FACING) == side.opposite
    }

    override fun updateTick(worldObj: World, pos: BlockPos, bs: IBlockState, rand: Random?) {
        val state = worldObj.getBlockState(pos).getValue(POWERED)
        val entitiesAround = worldObj.getEntitiesWithinAABB(EntityFallingBlock::class.java, AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 0, 2)))
        var flag: Boolean = false
        entitiesAround.filter { it.block?.block == ModBlocks.sand && it.block?.getValue(BlockSand.SAND_TYPE) == EnumSandType.BRIGHT }.forEach { flag = true }
        val expectedState = BrightsandPower.hasBrightsandPower(worldObj, pos, bs.getValue(BlockHorizontal.FACING).opposite) || flag
        if(state != expectedState && !worldObj.isRemote)
            worldObj.setBlockState(pos, worldObj.getBlockState(pos).withProperty(POWERED, expectedState), 1 or 2)
        worldObj.scheduleUpdate(pos, this, 0)
    }

    override fun getStrongPower(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Int {
        return blockState.getWeakPower(blockAccess, pos, side)
    }

   /* override fun getWeakPower(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Int {
        return if (!this.isPowered(blockState)) 0 else if (blockState.getValue(BlockHorizontal.FACING) == side) this.getActiveSignal(blockAccess, pos, blockState) else 0
    }*/

    override fun requiresUpdates(): Boolean {
        return true
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos?, state: IBlockState?) {
        worldIn.scheduleUpdate(pos, this, 0)
    }


}


