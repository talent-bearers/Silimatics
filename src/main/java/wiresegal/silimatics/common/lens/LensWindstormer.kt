package wiresegal.silimatics.common.lens

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.SoundCategory
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.core.ModSoundEvents
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper

open class LensWindstormer : ILens {
    companion object {
        val LOOK_THRESHOLD = Math.sin(Math.PI * 3 / 4)
        val TAG_TICK = "tick"
    }

    open val mainDiv: Vec3d by lazy { Vec3d(16.0, 20.0, 16.0) }
    open val secDiv: Vec3d by lazy { Vec3d(16.0, 16.0, 16.0) }
    open val pitch: Float by lazy { 1.0F }
    open val name: String by lazy { "windstormer" }

    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (player.isSneaking) {
            val tick = ItemNBTHelper.getInt(stack, TAG_TICK, 0)
            ItemNBTHelper.setInt(stack, TAG_TICK, tick + 1)
            val lookVec = player.lookVec
            if (tick % (20 / pitch).toInt() == 0)
                world.playSound(null, player.posX, player.posY, player.posZ, ModSoundEvents.WOOSH, SoundCategory.PLAYERS, 1f, pitch)
            player.motionX -= lookVec.xCoord / mainDiv.xCoord
            player.motionY -= lookVec.yCoord / mainDiv.yCoord
            player.motionZ -= lookVec.zCoord / mainDiv.zCoord
            val posVec = player.positionVector
            val range = 16.0
            val entities = world.getEntitiesWithinAABBExcludingEntity(player, player.entityBoundingBox.expand(range, range, range))
            for (entity in entities) if (entity is EntityLivingBase && entity.positionVector.subtract(posVec).lengthSquared() < range * range) {
                val dirVec = entity.positionVector.subtract(posVec).normalize()
                val dot = dirVec.dotProduct(lookVec)
                if (dot > LOOK_THRESHOLD) {
                    entity.motionX += lookVec.xCoord / secDiv.xCoord
                    entity.motionY += lookVec.yCoord / secDiv.yCoord
                    entity.motionZ += lookVec.zCoord / secDiv.zCoord
                }
            }
        } else
            ItemNBTHelper.setInt(stack, TAG_TICK, 0)
    }

    override fun onCleanupTick(world: World, entity: Entity, stack: ItemStack) {
        ItemNBTHelper.setInt(stack, TAG_TICK, 0)
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.$name.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.$name.desc2")
    }
}
