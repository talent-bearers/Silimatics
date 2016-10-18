package wiresegal.silimatics.common.block

import net.minecraft.block.material.Material
import net.minecraft.init.Blocks
import wiresegal.silimatics.common.lib.LibNames
import wiresegal.zenmodelloader.common.block.base.BlockMod

/**
 * Created by Elad on 10/18/2016.
 */
class BlockFusedStone : BlockMod(LibNames.FUSED_STONE, Material.ROCK) {
    init {
        setHardness(Blocks.STONE.getBlockHardness(null, null, null))
    }
}