package wiresegal.silimatics.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockModContainer
import com.teamwizardry.librarianlib.common.base.block.TileMod
import com.teamwizardry.librarianlib.common.util.autoregister.TileRegister
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.item.EnumSandType.Companion.capitalizeFirst
import wiresegal.silimatics.common.lib.LibNames
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by Elad on 10/13/2016.
 */
//https://www.youtube.com/watch?v=KJ6N1z3tGck
class BlockBrokenGlass : BlockModContainer("broken" + LibNames.GLASS.capitalizeFirst(), Material.GLASS) {
    init {
        setHardness(0.3F)
    }
    override fun createTileEntity(worldIn: World, blockstate: IBlockState): TileEntity = TileEntityBrokenGlass()

    override fun createItemForm(): ItemBlock? {
        return null
    }

    override fun getPickBlock(state: IBlockState?, target: RayTraceResult?, world: World?, pos: BlockPos?, player: EntityPlayer?): ItemStack {
        return ItemStack(ModBlocks.glass, 1, EnumSandType.HEART.ordinal)
    }

    override fun getDrops(world: IBlockAccess?, pos: BlockPos?, state: IBlockState?, fortune: Int): MutableList<ItemStack>? {
        val stack = ItemStack(ModItems.sand, 1, EnumSandType.HEART.ordinal)
        val list = mutableListOf(stack.copy(), stack.copy(), stack.copy())
        if(ThreadLocalRandom.current().nextBoolean()) list.add(stack.copy())
        return list
    }


    @TileRegister("letssingasongaboutbrokenglass")
    class TileEntityBrokenGlass : TileMod(), ITickable {
        @Save
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

    }
    companion object {
        val DURABILITY_IN_TICKS = 160
    }

}


