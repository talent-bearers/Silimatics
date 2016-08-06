package wiresegal.silimatics.common.lens

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.client.core.TooltipHelper

class LensTorturer : ILens {

    companion object {
        val TAG_COOLDOWN = "cooldown"
        val TAG_HITS = "hits"
        val TAG_UUID = "uuid"

        val MAX_HITS = 5
        val COOLDOWN = 25
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

    override fun onUsingTick(world: World, player: EntityPlayer, stack: ItemStack) {
        if (player.isSneaking) {
            val cooldown = ItemNBTHelper.getInt(stack, TAG_COOLDOWN, 0)
            val lookedAt = getEntityLookedAt(player, 10.0)
            if (lookedAt != null && lookedAt is EntityLivingBase) {
                if (cooldown == 0) {
                    val uuid = ItemNBTHelper.getUUID(stack, TAG_UUID, true)
                    val hits = if (uuid != lookedAt.uniqueID) 0 else ItemNBTHelper.getInt(stack, TAG_HITS, 0)
                    ItemNBTHelper.setUUID(stack, TAG_UUID, lookedAt.uniqueID)
                    lookedAt.attackEntityFrom(DamageSource.causePlayerDamage(player), Math.min(hits * 2f, lookedAt.health - 1))
                    lookedAt.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 15 * hits, 1))
                    if (hits < MAX_HITS)
                        ItemNBTHelper.setInt(stack, TAG_HITS, hits + 1)
                    ItemNBTHelper.setInt(stack, TAG_COOLDOWN, COOLDOWN)
                }
            } else {
                ItemNBTHelper.setInt(stack, TAG_HITS, 0)
                ItemNBTHelper.removeUUID(stack, TAG_UUID)
            }

            if (cooldown > 0)
                ItemNBTHelper.setInt(stack, TAG_COOLDOWN, cooldown - 1)
        } else {
            ItemNBTHelper.setInt(stack, TAG_COOLDOWN, 0)
            ItemNBTHelper.setInt(stack, TAG_HITS, 0)
            ItemNBTHelper.removeUUID(stack, TAG_UUID)
        }
    }

    override fun onCleanupTick(world: World, player: Entity, stack: ItemStack) {
        ItemNBTHelper.setInt(stack, TAG_COOLDOWN, 0)
        ItemNBTHelper.setInt(stack, TAG_HITS, 0)
        ItemNBTHelper.removeUUID(stack, TAG_UUID)
    }

    override fun addTooltip(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.torturer.desc1")
        TooltipHelper.addToTooltip(tooltip, "${LibMisc.MODID}.lens.torturer.desc2")
    }
}
