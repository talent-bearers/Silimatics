package wiresegal.silimatics.common.core

import net.minecraft.item.ItemArmor
import wiresegal.silimatics.common.item.ItemLens
import wiresegal.silimatics.common.item.ItemLensFrames
import wiresegal.silimatics.common.item.ItemSand
import wiresegal.silimatics.common.lib.LibNames

object ModItems {
    var lens: ItemLens
    var frame: ItemLensFrames
    var sand: ItemSand

    init {
        lens = ItemLens(LibNames.LENS)
        frame = ItemLensFrames(LibNames.LENS_FRAMES, ItemArmor.ArmorMaterial.CHAIN)
        sand = ItemSand(LibNames.SAND_ITEM)
    }
}
