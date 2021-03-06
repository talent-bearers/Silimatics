package wiresegal.silimatics.common.core

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
    val platform: BlockSilimaticPlatform
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
        platform = BlockSilimaticPlatform()
    }


}
