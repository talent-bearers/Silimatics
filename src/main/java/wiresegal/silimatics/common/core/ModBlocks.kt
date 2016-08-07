package wiresegal.silimatics.common.core

import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.silimatics.common.block.*
import wiresegal.silimatics.common.lib.LibNames

object ModBlocks {
    var sifter: BlockSifter
    var sand: BlockSand
    var glass: BlockGlass
    var glassPane: BlockGlassPane
    var lensGrinder: BlockLensGrinder

    init {
        sifter = BlockSifter(LibNames.SIFTER)
        sand = BlockSand(LibNames.SAND)
        glass = BlockGlass(LibNames.GLASS)
        glassPane = BlockGlassPane(LibNames.GLASS_PANE)
        lensGrinder = BlockLensGrinder(LibNames.GRINDER)

        GameRegistry.registerTileEntity(BlockGlass.TileSmedryGlass::class.java, "smedryglass")
        GameRegistry.registerTileEntity(BlockLensGrinder.TileLensGrinder::class.java, "smedrylensgrinder")
        GameRegistry.registerTileEntity(BlockSifter.TileBlockSifter::class.java, "smedrysifter")
    }
}
