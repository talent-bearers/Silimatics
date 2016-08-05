package wiresegal.silimatics.common.core

import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.silimatics.common.block.BlockGlass
import wiresegal.silimatics.common.block.BlockSand
import wiresegal.silimatics.common.block.BlockSifter

object ModBlocks {
    var sifter: BlockSifter
    var sand: BlockSand
    var glass: BlockGlass
    init {
        sifter = BlockSifter()
        sand = BlockSand("sand")
        glass = BlockGlass("glass")
        GameRegistry.registerTileEntity(BlockGlass.TileSmedryGlass::class.java, "smedryglass")
    }
}
