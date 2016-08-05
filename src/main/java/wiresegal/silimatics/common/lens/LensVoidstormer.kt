package wiresegal.silimatics.common.lens

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper

class LensVoidstormer : ILens {
    private val range = 8
    //north: 0, -1
    //east: 1, 0
    //south: 0, 1
    //west: -1, 0
    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        player.motionX += player.lookVec.xCoord / 8
        player.motionY += player.lookVec.normalize().yCoord / 8
        player.motionZ += player.lookVec.zCoord / 8
        val x = player.posX
        val y = player.posY
        val z = player.posZ
        if (player.lookVec.xCoord < 0.5 && player.lookVec.xCoord > -0.5 && player.lookVec.zCoord < -0.5) {//north, -z
            for(entityLivingBase in world.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(x - range, y - range, z, x + range, y + range, z - range), { (it !is EntityPlayer) })) {
                entityLivingBase.motionX += -player.lookVec.xCoord / 8
                entityLivingBase.motionY += -player.lookVec.yCoord / 8
                entityLivingBase.motionZ += -player.lookVec.zCoord / 8
            }

        } else if (player.lookVec.zCoord < 0.5 && player.lookVec.zCoord > -0.5 && player.lookVec.zCoord > 0.5) {//east, +x
            for(entityLivingBase in world.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(x, y - range, z - range, x + range, y + range, z + range), { (it !is EntityPlayer) })) {
                entityLivingBase.motionX += -player.lookVec.xCoord / 8
                entityLivingBase.motionY += -player.lookVec.yCoord / 8
                entityLivingBase.motionZ += -player.lookVec.zCoord / 8
            }

        } else if (player.lookVec.xCoord < 0.5 && player.lookVec.xCoord > -0.5 && player.lookVec.zCoord > 0.5) {//south +z
            for(entityLivingBase in world.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(x - range, y - range, z, x + range, y + range, z + range), { (it !is EntityPlayer) })) {
                entityLivingBase.motionX += -player.lookVec.xCoord / 8
                entityLivingBase.motionY += -player.lookVec.yCoord / 8
                entityLivingBase.motionZ += -player.lookVec.zCoord / 8
            }

        } else {
            for(entityLivingBase in world.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(x, y - range, z - range, x - range, y + range, z + range), { (it !is EntityPlayer) })) { //west -x
                entityLivingBase.motionX += -player.lookVec.xCoord / 8
                entityLivingBase.motionY += -player.lookVec.yCoord / 8
                entityLivingBase.motionZ += -player.lookVec.zCoord / 8
            }

        }

    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.voidstormer.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.voidstormer.desc2")
    }
}
