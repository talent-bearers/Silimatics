package wiresegal.silimatics.common.util

import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World

/**
 * Created by Elad on 10/22/2016.
 */
fun TextFormatting.color(): Int? = when(this) {
    TextFormatting.BLACK -> 0x000000
    TextFormatting.DARK_BLUE -> 0x0000AA
    TextFormatting.DARK_GREEN -> 0x00AA00
    TextFormatting.DARK_AQUA -> 0x00AAAA
    TextFormatting.DARK_RED -> 0xAA0000
    TextFormatting.DARK_PURPLE -> 0xAA00AA
    TextFormatting.GOLD -> 0xFFAA00
    TextFormatting.GRAY -> 0xAAAAAA
    TextFormatting.DARK_GRAY -> 0x555555
    TextFormatting.BLUE -> 0x5555FF
    TextFormatting.GREEN -> 0x55FF55
    TextFormatting.AQUA -> 0x55FFFF
    TextFormatting.RED -> 0xFF5555
    TextFormatting.LIGHT_PURPLE -> 0xFF55FF
    TextFormatting.YELLOW -> 0xFFFF55
    TextFormatting.WHITE -> 0xFFFFFF
    TextFormatting.OBFUSCATED -> null
    TextFormatting.BOLD -> null
    TextFormatting.STRIKETHROUGH -> null
    TextFormatting.UNDERLINE -> null
    TextFormatting.ITALIC -> null
    TextFormatting.RESET -> null
}
fun World.hasRedstoneSignalSimple(pos: BlockPos): Boolean {
    var flag: Boolean = false
    for (facing in EnumFacing.values()) if(getRedstonePower(pos, facing) > 0 || isSidePowered(pos, facing) || getStrongPower(pos, facing) > 0) flag = true
    flag = flag || isBlockPowered(pos) || getStrongPower(pos) > 0 || isBlockIndirectlyGettingPowered(pos) > 0
    return flag
}


