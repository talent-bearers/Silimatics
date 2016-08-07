package wiresegal.silimatics.common.core

import net.minecraft.item.ItemArmor
import wiresegal.silimatics.common.Silimatics
import wiresegal.silimatics.common.item.*
import wiresegal.silimatics.common.lib.LibNames

object ModItems {
    val lens: ItemLens
    val frame: ItemLensFrames
    val sand: ItemSand
    val boots: ItemGrapplerBoots
    lateinit var debugger: ItemDebugger

    init {
        lens = ItemLens(LibNames.LENS)
        frame = ItemLensFrames(LibNames.LENS_FRAMES, ItemArmor.ArmorMaterial.CHAIN)
        sand = ItemSand(LibNames.SAND_ITEM)
        boots = ItemGrapplerBoots(LibNames.GRAPPLER)
        if (Silimatics.isDevEnv) debugger = ItemDebugger(LibNames.DEBUGGER)
    }
}
