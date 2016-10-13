package wiresegal.silimatics.common.block

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.item.EnumSandType.Companion.capitalizeFirst
import wiresegal.silimatics.common.lib.LibNames
import wiresegal.zenmodelloader.common.block.base.BlockModContainer
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by Elad on 10/13/2016.
 */
//https://www.youtube.com/watch?v=KJ6N1z3tGck
class BlockBrokenGlass : BlockModContainer("broken" + LibNames.GLASS.capitalizeFirst(), Material.GLASS) {
    init {
        setHardness(0.3F)
        GameRegistry.registerTileEntity(TileEntityBrokenGlass::class.java, "aSongWeSingAboutBrokenGlass")
    }
    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity = TileEntityBrokenGlass()

    override fun createItemForm(): ItemBlock? {
        return null
    }

    override fun getPickBlock(state: IBlockState?, target: RayTraceResult?, world: World?, pos: BlockPos?, player: EntityPlayer?): ItemStack? {
        return ItemStack(ModBlocks.glass, 1, EnumSandType.HEART.ordinal)
    }

    override fun getDrops(world: IBlockAccess?, pos: BlockPos?, state: IBlockState?, fortune: Int): MutableList<ItemStack>? {
        val stack = ItemStack(ModItems.sand, 1)
        val list = mutableListOf(stack.copy(), stack.copy(), stack.copy())
        if(ThreadLocalRandom.current().nextBoolean()) list.add(stack.copy())
        return list
    }

    class TileEntityBrokenGlass : TileMod(), ITickable {
        var ticks: Int = -1
        override fun update() {
            if(ticks == -1) return
            if(ticks >= DURABILITY_IN_TICKS) {
                ticks = -1
                worldObj.setBlockState(pos, ModBlocks.glass.defaultState.withProperty(BlockGlass.SAND_TYPE, EnumSandType.HEART))
            }
            else ticks++
            markDirty()
        }

        override fun writeCustomNBT(cmp: NBTTagCompound) {
            super.writeCustomNBT(cmp)
            cmp.setInteger("ticks", ticks)
        }

        override fun readCustomNBT(cmp: NBTTagCompound) {
            super.readCustomNBT(cmp)
            ticks = cmp.getInteger("ticks")
        }

    }
    companion object {
        val DURABILITY_IN_TICKS = 160
    }

}


