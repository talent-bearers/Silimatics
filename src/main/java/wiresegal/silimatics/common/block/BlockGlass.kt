package wiresegal.silimatics.common.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.init.Blocks
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemBlock
import net.minecraft.potion.PotionEffect
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.zenmodelloader.common.block.base.BlockMod
import wiresegal.zenmodelloader.common.block.base.ItemModBlock
import wiresegal.zenmodelloader.common.core.IBlockColorProvider
import java.util.*

/**
 * @author WireSegal
 * Created at 9:30 AM on 8/4/16.
 */
class BlockGlass(name: String) : BlockMod(name, Material.GLASS, *EnumSandType.getSandTypeNamesFor(name)), IBlockColorProvider {

    companion object {
        val SAND_TYPE = PropertyEnum.create("sand", EnumSandType::class.java)
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, SAND_TYPE)
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockColor(): IBlockColor {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(SAND_TYPE).color }
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i -> EnumSandType.values()[itemStack.itemDamage % EnumSandType.values().size].color }
    }

    override fun createItemForm(): ItemBlock? {
        return ItemModBlock(this)
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess?, pos: BlockPos?): Int {
        return if (state.getValue(SAND_TYPE) == EnumSandType.BRIGHT) 15 else 0
    }

    override fun tickRate(worldIn: World?): Int {
        return 1
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos?, state: IBlockState?) {
        worldIn.scheduleUpdate(pos, this, tickRate(worldIn))
    }

    override fun onEntityCollidedWithBlock(worldIn: World, pos: BlockPos, state: IBlockState, entityIn: Entity) {
        entityIn.attackEntityFrom(DamageSource.cactus, 1.0f)
    }

    override fun updateTick(worldIn: World, pos: BlockPos, state: IBlockState, rand: Random?) {
        worldIn.scheduleUpdate(pos, this, tickRate(worldIn))

        if (state.getValue(SAND_TYPE) == EnumSandType.BLOOD) {
            val entities = worldIn.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(pos.up()))
            for (entity in entities) {
                entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 5, 3))
                entity.addPotionEffect(PotionEffect(MobEffects.STRENGTH, 5))
                entity.addPotionEffect(PotionEffect(MobEffects.RESISTANCE, 5))
            }
        }


        if (state.getValue(SAND_TYPE) == EnumSandType.STORM || state.getValue(SAND_TYPE) == EnumSandType.VOID) {
            val entities = worldIn.getEntitiesWithinAABB(EntityLivingBase::class.java,
                    AxisAlignedBB(pos).expand(5.0, 5.0, 5.0)) {
                (it is EntityLivingBase && it.isNonBoss) || (it is IProjectile)
            }

            pushEntities(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 5.0, if (state.getValue(SAND_TYPE) == EnumSandType.STORM) 0.4 else -0.4, entities)
        }

    }

    fun pushEntities(x: Double, y: Double, z: Double, range: Double, velocity: Double, entities: List<Entity>) {
        for (entity in entities) {
            val xDif = entity.posX - x
            val yDif = entity.posY - (y + 1)
            val zDif = entity.posZ - z
            val vec = Vec3d(xDif, yDif, zDif).normalize()
            val dist = xDif * xDif + yDif * yDif + zDif * zDif
            if (dist <= range * range) {
                entity.motionX = velocity * vec.xCoord
                entity.motionY = velocity * vec.yCoord
                entity.motionZ = velocity * vec.zCoord
                entity.fallDistance = 0f
            }
        }
    }
}
