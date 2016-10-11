package wiresegal.silimatics.common.util

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import wiresegal.silimatics.common.block.BlockSand
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.item.EnumSandType

object BrightsandPower {
    fun hasBrightsandPower(world: IBlockAccess, pos: BlockPos): Boolean {
        for(facing in EnumFacing.values())
            if (world.getBlockState(pos.add(facing.directionVec)).block == ModBlocks.sand
                    && world.getBlockState(pos.add(facing.directionVec)).getValue(BlockSand.SAND_TYPE) == EnumSandType.BRIGHT) return true
        return false
    }
    fun hasBrightsandPowerAndRedstonePower(world: World, pos: BlockPos): Boolean {
        var flag: Boolean = false
        for (facing in EnumFacing.values()) if(world.getRedstonePower(pos, facing) > 0 || world.isSidePowered(pos, facing) || world.getStrongPower(pos, facing) > 0) flag = true
        flag = flag || world.isBlockPowered(pos) || world.getStrongPower(pos) > 0 || world.isBlockIndirectlyGettingPowered(pos) > 0
        return hasBrightsandPower(world, pos) && flag
    }

}
