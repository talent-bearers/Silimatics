package wiresegal.silimatics.common.lens

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper

class LensVoidstormer : ILens {
    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        player.motionX += player.lookVec.xCoord / 20
        player.motionY += player.lookVec.yCoord / 20
        player.motionZ += player.lookVec.zCoord / 20
        //todo
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.voidstormer.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.voidstormer.desc2")
    }
}
