package wiresegal.silimatics.common.core

import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.silimatics.common.block.BlockGlass
import wiresegal.silimatics.common.block.BlockGlassPane
import wiresegal.silimatics.common.block.BlockSand
import wiresegal.silimatics.common.block.BlockSifter
import wiresegal.silimatics.common.lib.LibNames

object ModBlocks {
    var sifter: BlockSifter
    var sand: BlockSand
    var glass: BlockGlass
    var glassPane: BlockGlassPane

    init {
        sifter = BlockSifter(LibNames.SIFTER)
        sand = BlockSand(LibNames.SAND)
        glass = BlockGlass(LibNames.GLASS)
        glassPane = BlockGlassPane(LibNames.GLASS_PANE)

        GameRegistry.registerTileEntity(BlockGlass.TileSmedryGlass::class.java, "smedryglass")
    }
}
