package wiresegal.silimatics.common.block

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.init.Blocks
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.core.ModCreativeTab
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.zenmodelloader.common.block.base.BlockMod
import wiresegal.zenmodelloader.common.core.IBlockColorProvider
import java.util.*

/**
 * @author WireSegal
 * Created at 9:30 AM on 8/4/16.
 */
class BlockSand(name: String) : BlockMod(name, Material.SAND, *EnumSandType.getSandTypeNamesFor(name)), IBlockColorProvider {


    companion object {
        val SAND_TYPE = PropertyEnum.create("sand", EnumSandType::class.java)
    }

    init {
        setHardness(0.5F)
        soundType = SoundType.SAND
        ModCreativeTab.set(this)
    }

    override fun isToolEffective(type: String, state: IBlockState): Boolean {
        return type == "shovel"
    }

    override fun getHarvestTool(state: IBlockState?): String {
        return "shovel"
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, SAND_TYPE)
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockColor(): IBlockColor {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(SAND_TYPE).color }
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i -> EnumSandType.values()[itemStack.itemDamage % EnumSandType.values().size].color }
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess?, pos: BlockPos?): Int {
        return if (state.getValue(SAND_TYPE) == EnumSandType.BRIGHT) 15 else 0
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(SAND_TYPE).ordinal
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(SAND_TYPE, EnumSandType.values()[meta % EnumSandType.values().size])
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn))
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn))
    }

    override fun damageDropped(state: IBlockState): Int {
        return getMetaFromState(state)
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!worldIn.isRemote) {
            this.checkFallable(worldIn, pos)
        }
    }

    private fun checkFallable(worldIn: World, pos: BlockPos) {
        if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.y >= 0) {

            if (!worldIn.isRemote) {
                val entityfallingblock = EntityFallingBlock(worldIn, pos.x.toDouble() + 0.5, pos.y.toDouble(), pos.z.toDouble() + 0.5, worldIn.getBlockState(pos))
                worldIn.spawnEntityInWorld(entityfallingblock)
            }
        }
    }

    override fun tickRate(worldIn: World): Int = 2

    fun canFallThrough(state: IBlockState): Boolean {
        val block = state.block
        val material = state.material
        return block === Blocks.FIRE || material === Material.AIR || material === Material.WATER || material === Material.LAVA
    }

    @SideOnly(Side.CLIENT)
    override fun randomDisplayTick(stateIn: IBlockState, worldIn: World, pos: BlockPos, rand: Random) {
        if (rand.nextInt(16) == 0) {
            val blockpos = pos.down()

            if (canFallThrough(worldIn.getBlockState(blockpos))) {
                val d0 = (pos.x.toFloat() + rand.nextFloat()).toDouble()
                val d1 = pos.y.toDouble() - 0.05
                val d2 = (pos.z.toFloat() + rand.nextFloat()).toDouble()
                worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, d0, d1, d2, 0.0, 0.0, 0.0, Block.getStateId(stateIn))
            }
        }
    }

}


