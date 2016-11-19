package wiresegal.silimatics.common.util

import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import wiresegal.silimatics.api.event.BrightsandEvent
import wiresegal.silimatics.common.block.BlockSand
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.item.EnumSandType

object BrightsandPower {
    fun hasBrightsandPower(world: World, pos: BlockPos, orBSEntity: Boolean = true): Boolean {
        for (facing in EnumFacing.values()) {
            if (world.getBlockState(pos.add(facing.directionVec)).block == ModBlocks.sand
                    && world.getBlockState(pos.add(facing.directionVec)).getValue(BlockSand.SAND_TYPE) == EnumSandType.BRIGHT) return true
        }
        if (orBSEntity) {
            val entitiesAround = world.getEntitiesWithinAABB(EntityFallingBlock::class.java, AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 0, 2)))
            entitiesAround.filter { it.block?.block == ModBlocks.sand && it.block?.getValue(BlockSand.SAND_TYPE) == EnumSandType.BRIGHT }.forEach { return true }
        }
        val event = BrightsandEvent(pos, world)
        MinecraftForge.EVENT_BUS.post(event)
        if(event.isPowered) return true
        return SilimaticEvents.tracker.map { it.toLong() }.contains(pos.toLong())
    }

    fun hasBrightsandPower(world: World, pos: BlockPos, facing0: EnumFacing, orBSEntity: Boolean = true): Boolean {
        if (world.getBlockState(pos.add(facing0.directionVec)).block == ModBlocks.sand
                && world.getBlockState(pos.add(facing0.directionVec)).getValue(BlockSand.SAND_TYPE) == EnumSandType.BRIGHT) return true
        if (orBSEntity) {
            val entitiesAround = world.getEntitiesWithinAABB(EntityFallingBlock::class.java, AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 0, 2)))
            entitiesAround.filter { it.block?.block == ModBlocks.sand && it.block?.getValue(BlockSand.SAND_TYPE) == EnumSandType.BRIGHT }.forEach { return true }
        }
        val event = BrightsandEvent(pos, world)
        MinecraftForge.EVENT_BUS.post(event)
        if(event.isPowered) return true
        return SilimaticEvents.tracker.map { it.toLong() }.contains(pos.toLong())
    }

    fun hasBrightsandPowerAndRedstonePower(world: World, pos: BlockPos): Boolean {
        var flag: Boolean = false
        for (facing in EnumFacing.values()) if (world.getRedstonePower(pos, facing) > 0 || world.isSidePowered(pos, facing) || world.getStrongPower(pos, facing) > 0) flag = true
        flag = flag || world.isBlockPowered(pos) || world.getStrongPower(pos) > 0 || world.isBlockIndirectlyGettingPowered(pos) > 0
        return hasBrightsandPower(world, pos) && flag
    }

    fun hasRedstonePower(world: World, pos: BlockPos): Boolean {
        var flag: Boolean = false
        for (facing in EnumFacing.values()) if (world.getRedstonePower(pos, facing) > 0 || world.isSidePowered(pos, facing) || world.getStrongPower(pos, facing) > 0) flag = true
        flag = flag || world.isBlockPowered(pos) || world.getStrongPower(pos) > 0 || world.isBlockIndirectlyGettingPowered(pos) > 0
        return flag
    }

    fun hasRedstonePowerOrNeighbors(world: World, pos: BlockPos): Boolean {
        var flag: Boolean = false
        for (f in EnumFacing.values()) {
            for (facing in EnumFacing.values()) if (world.getRedstonePower(pos.offset(f), facing) > 0 || world.isSidePowered(pos.offset(f), facing) || world.getStrongPower(pos.offset(f), facing) > 0) flag = true
            flag = flag || world.isBlockPowered(pos.offset(f)) || world.getStrongPower(pos.offset(f)) > 0 || world.isBlockIndirectlyGettingPowered(pos.offset(f)) > 0
        }
        return flag
    }

}
