package wiresegal.silimatics.common.lens

import com.teamwizardry.librarianlib.client.util.TooltipHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.silimatics.common.potions.ModPotions

object LensOculator : ILens {

    val TAG_WARMUP = "warmup"
    val TAG_UUID = "uuid"

    val WARMUP_TIME = 20
    fun getEntityLookedAt(e: Entity, maxDistance: Double = 32.0): Entity? {
        var foundEntity: Entity? = null
        var distance = maxDistance
        val pos = raycast(e, maxDistance)
        var positionVector = e.positionVector
        if (e is EntityPlayer) {
            positionVector = positionVector.addVector(0.0, e.getEyeHeight().toDouble(), 0.0)
        }

        if (pos != null) {
            distance = pos.hitVec.distanceTo(positionVector)
        }

        val lookVector = e.lookVec
        val reachVector = positionVector.addVector(lookVector.xCoord * maxDistance, lookVector.yCoord * maxDistance, lookVector.zCoord * maxDistance)
        var lookedEntity: Entity? = null
        val entitiesInBoundingBox = e.worldObj.getEntitiesWithinAABBExcludingEntity(e, e.entityBoundingBox.addCoord(lookVector.xCoord * maxDistance, lookVector.yCoord * maxDistance, lookVector.zCoord * maxDistance).expand(1.0, 1.0, 1.0))
        var minDistance = distance
        val var14 = entitiesInBoundingBox.iterator()

        while (true) {
            do {
                do {
                    if (!var14.hasNext()) {
                        return foundEntity
                    }
                    val next = var14.next()
                    if (next.canBeCollidedWith()) {
                        val collisionBorderSize = next.collisionBorderSize
                        val hitbox = next.entityBoundingBox.expand(collisionBorderSize.toDouble(), collisionBorderSize.toDouble(), collisionBorderSize.toDouble())
                        val interceptPosition = hitbox.calculateIntercept(positionVector, reachVector)
                        if (hitbox.isVecInside(positionVector)) {
                            if (0.0 < minDistance || minDistance == 0.0) {
                                lookedEntity = next
                                minDistance = 0.0
                            }
                        } else if (interceptPosition != null) {
                            val distanceToEntity = positionVector.distanceTo(interceptPosition.hitVec)
                            if (distanceToEntity < minDistance || minDistance == 0.0) {
                                lookedEntity = next
                                minDistance = distanceToEntity
                            }
                        }
                    }
                } while (lookedEntity == null)
            } while (minDistance >= distance && pos != null)

            foundEntity = lookedEntity
        }
    }

    fun raycast(e: Entity, len: Double, stopOnLiquid: Boolean = false): RayTraceResult? {
        val vec = Vec3d(e.posX, e.posY, e.posZ).addVector(0.0, if (e is EntityPlayer) e.getEyeHeight().toDouble() else 0.0, 0.0)

        val look = e.lookVec
        if (look == null) {
            return null
        } else {
            return raycast(e.worldObj, vec, look, len, stopOnLiquid)
        }
    }

    fun raycast(world: World, origin: Vec3d, ray: Vec3d, len: Double, stopOnLiquid: Boolean = false): RayTraceResult? {
        val end = origin.add(ray.normalize().scale(len))
        val pos = world.rayTraceBlocks(origin, end, stopOnLiquid)
        return pos
    }


    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (player.isSneaking) {
            val warmup = ItemNBTHelper.getInt(stack, TAG_WARMUP, 0)
            val lookedAt = getEntityLookedAt(player, 10.0)
            if (lookedAt != null && lookedAt is EntityLivingBase) {
                if (warmup == WARMUP_TIME) {
                    val uuid = ItemNBTHelper.getUUID(stack, TAG_UUID, true)
                    ItemNBTHelper.setUUID(stack, TAG_UUID, lookedAt.uniqueID)
                    if (uuid != lookedAt.uniqueID)
                        ItemNBTHelper.setInt(stack, TAG_WARMUP, 0)
                    else
                        lookedAt.addPotionEffect(PotionEffect(ModPotions.disoriented, 5))
                }
            } else {
                ItemNBTHelper.setInt(stack, TAG_WARMUP, 0)
                ItemNBTHelper.removeUUID(stack, TAG_UUID)
            }

            if (warmup < WARMUP_TIME)
                ItemNBTHelper.setInt(stack, TAG_WARMUP, warmup + 1)
        } else {
            ItemNBTHelper.setInt(stack, TAG_WARMUP, 0)
            ItemNBTHelper.removeUUID(stack, TAG_UUID)
        }
    }

    override fun onCleanupTick(world: World, player: Entity, stack: ItemStack) {
        ItemNBTHelper.setInt(stack, TAG_WARMUP, 0)
        ItemNBTHelper.removeUUID(stack, TAG_UUID)
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.oculator.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.oculator.desc2")
    }
}
