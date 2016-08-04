package wiresegal.silimatics.common.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.init.Blocks
import net.minecraft.item.ItemBlock
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.zenmodelloader.common.block.base.BlockMod
import wiresegal.zenmodelloader.common.block.base.ItemModBlock
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

    override fun createItemForm(): ItemBlock? {
        return ItemModBlock(this)
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos, state: IBlockState) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn))
    }

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn))
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random) {
        if (!worldIn.isRemote) {
            this.checkFallable(worldIn, pos)
        }
    }

    private fun checkFallable(worldIn: World, pos: BlockPos) {
        if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.y >= 0) {

            worldIn.setBlockToAir(pos)
            var blockpos = pos.down()
            while ((worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.y > 0) {
                blockpos = blockpos.down()
            }

            if (blockpos.y > 0) {
                worldIn.setBlockState(blockpos.up(), this.defaultState)
            }
        }
    }

    override fun tickRate(worldIn: World): Int {
        return 2
    }

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
