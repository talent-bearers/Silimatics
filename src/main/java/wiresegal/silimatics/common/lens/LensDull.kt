package wiresegal.silimatics.common.lens

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens

class LensDull : ILens {
    override fun shouldMarkAsOculator(stack: ItemStack): Boolean {
        return false
    }
}
