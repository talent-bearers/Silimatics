package wiresegal.silimatics.common.core

import net.minecraftforge.fml.common.registry.GameRegistry
import wiresegal.silimatics.common.block.*
import wiresegal.silimatics.common.lib.LibNames

object ModBlocks {
    val sifter: BlockSifter
    val sand: BlockSand
    val glass: BlockGlass
    val glassPane: BlockGlassPane
    val lensGrinder: BlockLensGrinder
    val detector: BlockBrightsandDetector
    val cymaticPlate: BlockCymaticPlate
    val brokenGlass: BlockBrokenGlass
    val fusedStone: BlockFusedStone
    init {
        sifter = BlockSifter(LibNames.SIFTER)
        sand = BlockSand(LibNames.SAND)
        glass = BlockGlass(LibNames.GLASS)
        glassPane = BlockGlassPane(LibNames.GLASS_PANE)
        lensGrinder = BlockLensGrinder(LibNames.GRINDER)
        detector = BlockBrightsandDetector()
        cymaticPlate = BlockCymaticPlate()
        brokenGlass = BlockBrokenGlass()
        fusedStone = BlockFusedStone()
        GameRegistry.registerTileEntity(BlockGlass.TileSmedryGlass::class.java, "smedryglass")
        GameRegistry.registerTileEntity(BlockLensGrinder.TileLensGrinder::class.java, "smedrylensgrinder")
        GameRegistry.registerTileEntity(BlockSifter.TileBlockSifter::class.java, "smedrysifter")
    }


}
