package wiresegal.silimatics.common.lens

import net.minecraft.item.ItemStack
import wiresegal.silimatics.api.lens.ILens

class LensDull : ILens {
    override fun shouldMarkAsOculator(stack: ItemStack): Boolean {
        return false
    }
}
