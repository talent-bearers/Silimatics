package wiresegal.silimatics.common.core

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.silimatics.common.block.BlockGlass
import wiresegal.silimatics.common.block.BlockSand
import wiresegal.silimatics.common.block.BlockSifter
import wiresegal.silimatics.common.lib.LibNames

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
