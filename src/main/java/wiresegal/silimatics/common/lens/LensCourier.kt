package wiresegal.silimatics.common.lens

import com.teamwizardry.librarianlib.client.util.TooltipHelper
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.lens.courier.CourierManager
import wiresegal.silimatics.common.lib.LibMisc

object LensCourier : ILens {
    val manager: CourierManager = CourierManager()


    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        //todo
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.lensView.desc")
    }
}
