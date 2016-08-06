package wiresegal.silimatics.common.lens

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper

class LensWindstormer : ILens {


    companion object {
        val LOOK_THRESHOLD = Math.sin(Math.PI * 3 / 4)
    }

    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (player.isSneaking) {
            val lookVec = player.lookVec
            player.motionX -= lookVec.xCoord / 16
            player.motionY -= lookVec.yCoord / 20
            player.motionZ -= lookVec.zCoord / 16
            val posVec = player.positionVector
            val range = 16.0
            val entities = world.getEntitiesWithinAABBExcludingEntity(player, player.entityBoundingBox.expand(range, range, range))
            for (entity in entities) if (entity is EntityLivingBase && entity.positionVector.subtract(posVec).lengthSquared() < range * range) {
                val dirVec = entity.positionVector.subtract(posVec).normalize()
                val dot = dirVec.dotProduct(lookVec)
                if (dot > LOOK_THRESHOLD) {
                    entity.motionX += lookVec.xCoord / 16
                    entity.motionY += lookVec.yCoord / 16
                    entity.motionZ += lookVec.zCoord / 16
                }
            }
        }
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.windstormer.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.windstormer.desc2")
    }
}
