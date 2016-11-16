package wiresegal.silimatics.common.compat.refraction

import com.teamwizardry.refraction.api.Effect
import com.teamwizardry.refraction.common.light.EffectTracker
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import wiresegal.silimatics.common.block.BlockGlass
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.lens.LensOculator
import java.awt.Color

/**
 * Created by Elad on 11/16/2016.
 */
class SilifractionEffect(val state: IBlockState, val origin: BlockPos) : Effect() {
    init {
        EffectTracker.registerEffect(this)
    }

    override fun getCooldown(): Int {
        return if (potency == 0) 0 else 1000 / potency
    }

    override fun run(worldObj: World, locations: Set<BlockPos>) {
        val type = state.getValue(BlockGlass.SAND_TYPE)
        for(pos in locations)
            when(type) {
                EnumSandType.BLOOD -> {

                    val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, state.getBoundingBox(worldObj, pos).offset(pos.up()))
                    if (!worldObj.isRemote) for (entity in entities) {
                        entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 5, 3))
                        entity.addPotionEffect(PotionEffect(MobEffects.STRENGTH, 5))
                        entity.addPotionEffect(PotionEffect(MobEffects.RESISTANCE, 5))
                    }
                }
                EnumSandType.BRIGHT -> {
                    //noop; brightglass does not do stuff
                }
                EnumSandType.DULL -> {
                    //noop; dullglass does not do stuff
                }
                EnumSandType.RASHID -> {
                    //noop; rashidglass does not do stuff
                }
                EnumSandType.HEAT -> {
                    /*val poses = Sets.newHashSet<BlockPos>()
                    for(pos0 in locations) {
                        for (facing in EnumFacing.VALUES)
                            if (BrightsandPower.hasBrightsandPower(worldObj, pos.offset(facing)))
                                poses.add(pos0)
                    }
                    for(pos0 in poses) {
                        worldObj.createExplosion(null, pos0.x.toDouble(), pos0.y.toDouble(), pos0.z.toDouble(), 8f, true)
                        worldObj.setBlockToAir(origin)
                    }*/
                    if(beam?.trace?.typeOfHit == RayTraceResult.Type.ENTITY) {
                        val entity = beam.trace.entityHit
                        worldObj.createExplosion(null, entity.posX, entity.posY, entity.posZ, 8f, true)
                        worldObj.setBlockToAir(origin)
                    }
                }
                EnumSandType.STORM, EnumSandType.VOID -> {
                    val range = 4.0
                    val entities = worldObj.getEntitiesWithinAABB(Entity::class.java,
                            state.getBoundingBox(worldObj, pos).offset(pos).expand(range, range, range)) {
                        (it is EntityLivingBase && it.isNonBoss) || (it is IProjectile) || (it is EntityItem)
                    }
                    pushEntities(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, range, if (state.getValue(BlockGlass.SAND_TYPE) == EnumSandType.STORM) 0.05 else -0.05, entities)
                }
                EnumSandType.TRAIL -> {
                    val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, state.getBoundingBox(worldObj, pos).offset(pos).expand(1.125, 1 + 1 / 32.0, 1.125).offset(-1.0, -1 / 32.0 - 1, -1.0))
                    for (entity in entities) {
                        if (entity.getItemStackFromSlot(EntityEquipmentSlot.FEET)?.item == ModItems.boots) {
                            entity.posY += 5
                            entity.fallDistance = 0F
                        }
                    }
                    //why yes, grappler's laser
                }
                EnumSandType.PAIN -> {
                    val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, state.getBoundingBox(worldObj, pos).offset(pos).expand(1 / 16.0, 1 / 16.0, 1 / 16.0))
                    for (entity in entities)
                        entity.attackEntityFrom(DamageSource.cactus, 1f)
                }
                EnumSandType.SUN -> {
                    for (player in worldObj.playerEntities) {
                        val blockState = getBlockLookedAt(player)
                        if (blockState != null && (blockState.block == ModBlocks.glassPane || blockState.block == ModBlocks.glass) && blockState.getValue(BlockGlass.SAND_TYPE) == EnumSandType.SUN) player.addPotionEffect(PotionEffect(MobEffects.BLINDNESS, 100, 0))
                    }
                }
                EnumSandType.HEART -> {
                    //noop; nah
                }
                EnumSandType.SCHOOL -> {
                    for (pos0 in BlockPos.getAllInBox(pos.add(-3, -3, -3), pos.add(4, 4, 4))) if (worldObj.getTileEntity(pos0) is ITickable && worldObj.getTileEntity(pos0) !is BlockGlass.TileSmedryGlass) (worldObj.getTileEntity(pos0) as ITickable).update()
                }
                EnumSandType.VIEW -> {
                    //noop; viewglass does not do stuff
                }
                null -> throw NullPointerException()
            }
    }

    override fun getColor(): Color {
        return Color(state.getValue(BlockGlass.SAND_TYPE).glassColor)
    }

    fun getBlockLookedAt(e: Entity, maxDistance: Double = 32.0): IBlockState? {
        val pos = LensOculator.raycast(e, maxDistance)
        return if (pos != null && pos.typeOfHit == RayTraceResult.Type.BLOCK) e.worldObj.getBlockState(pos.blockPos) else null
    }

    fun pushEntities(x: Double, y: Double, z: Double, range: Double, velocity: Double, entities: List<Entity>) {
        for (entity in entities) {
            if (entity is EntityPlayer && entity.capabilities.isFlying) continue
            val xDif = entity.posX - x
            val yDif = entity.posY - y
            val zDif = entity.posZ - z
            val motionMul = if (entity is EntityPlayer && entity.isSneaking) 0.175 else 1.0
            val vec = Vec3d(xDif, yDif, zDif).normalize()
            val dist = xDif * xDif + yDif * yDif + zDif * zDif
            if (dist <= range * range &&
                    Math.abs(entity.motionX + motionMul * velocity * vec.xCoord) < motionMul * Math.abs(velocity) * 10 &&
                    Math.abs(entity.motionY + motionMul * velocity * vec.yCoord) < motionMul * Math.abs(velocity) * 10 &&
                    Math.abs(entity.motionZ + motionMul * velocity * vec.zCoord) < motionMul * Math.abs(velocity) * 10) {
                entity.motionX += motionMul * velocity * vec.xCoord
                entity.motionY += motionMul * velocity * vec.yCoord * 1.5
                entity.motionZ += motionMul * velocity * vec.zCoord
                if (velocity * vec.yCoord > 0) entity.fallDistance = 0f
            }
        }
    }
}