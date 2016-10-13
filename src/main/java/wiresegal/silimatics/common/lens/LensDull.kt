package wiresegal.silimatics.common.lens

import net.minecraft.item.ItemStack
import wiresegal.silimatics.api.lens.ILens

object LensDull : ILens {
    override fun shouldMarkAsOculator(stack: ItemStack): Boolean {
        return false
    }
}
