
package wiresegal.silimatics.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockMod
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.init.Blocks
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import wiresegal.silimatics.common.lib.LibNames
import wiresegal.silimatics.common.util.BrightsandPower

/**
 * Created by Elad on 10/30/2016.
 */
class BlockSilimaticPlatform : BlockMod(LibNames.PLATFORM, Material.ROCK) {
    init {
        setHardness(Blocks.STONE.getBlockHardness(null, null, null))
    }

    override fun addCollisionBoxToList(state: IBlockState?, worldIn: World, pos: BlockPos, entityBox: AxisAlignedBB?, collidingBoxes: MutableList<AxisAlignedBB>?, par7Entity: Entity?) {
        if(canPass(worldIn, par7Entity, pos))
            super.addCollisionBoxToList(state, worldIn, pos, entityBox, collidingBoxes, par7Entity)
    }

    fun canPass(worldIn: World, par7Entity: Entity?, pos: BlockPos): Boolean {
        return (par7Entity != null && par7Entity.motionY < 0) || !BrightsandPower.hasBrightsandPower(worldIn, pos)
    }

}