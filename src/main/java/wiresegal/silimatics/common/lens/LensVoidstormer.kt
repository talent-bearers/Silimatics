package wiresegal.silimatics.common.lens

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ModSoundEvents
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper

class LensVoidstormer : LensWindstormer() {

    override val mainDiv: Int
        get() = -16
    override val ySecDiv: Int
        get() = -20
    override val pitch: Float
        get() = 0.75F
    override val name: String
        get() = "voidstormer"
}
