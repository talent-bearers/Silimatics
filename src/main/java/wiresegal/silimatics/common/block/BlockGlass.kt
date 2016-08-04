package wiresegal.silimatics.common.block

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.init.MobEffects
import net.minecraft.item.ItemBlock
import net.minecraft.potion.PotionEffect
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.DamageSource
import net.minecraft.util.ITickable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.zenmodelloader.common.block.base.BlockModContainer
import wiresegal.zenmodelloader.common.block.base.ItemModBlock
import wiresegal.zenmodelloader.common.core.IBlockColorProvider

/**
 * @author WireSegal
 * Created at 9:30 AM on 8/4/16.
 */
class BlockGlass(name: String) : BlockModContainer(name, Material.GLASS, *EnumSandType.getSandTypeNamesFor(name)), IBlockColorProvider {

    companion object {
        val SAND_TYPE = PropertyEnum.create("sand", EnumSandType::class.java)
    }

    init {
        setHardness(0.3F)
        soundType = SoundType.GLASS
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, SAND_TYPE)
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockColor(): IBlockColor {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(SAND_TYPE).glassColor }
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i -> EnumSandType.values()[itemStack.itemDamage % EnumSandType.values().size].glassColor }
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(SAND_TYPE).ordinal
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(SAND_TYPE, EnumSandType.values()[meta % EnumSandType.values().size])
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess?, pos: BlockPos?): Int {
        return if (state.getValue(SAND_TYPE) == EnumSandType.BRIGHT) 15 else 0
    }

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity {
        return TileSmedryGlass()
    }

    override fun damageDropped(state: IBlockState): Int {
        return getMetaFromState(state)
    }

    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.TRANSLUCENT
    }

    override fun isFullCube(state: IBlockState): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    class TileSmedryGlass : TileMod(), ITickable {
        override fun update() {
            if (worldObj.isBlockPowered(pos)) return
            val state = worldObj.getBlockState(pos)
            if (state.getValue(SAND_TYPE) == EnumSandType.BLOOD) {
                val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(pos.up()))
                if (!worldObj.isRemote) for (entity in entities) {
                    entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 5, 3))
                    entity.addPotionEffect(PotionEffect(MobEffects.STRENGTH, 5))
                    entity.addPotionEffect(PotionEffect(MobEffects.RESISTANCE, 5))
                }
            } else if (state.getValue(SAND_TYPE) == EnumSandType.STORM || state.getValue(SAND_TYPE) == EnumSandType.VOID) {
                val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java,
                        AxisAlignedBB(pos).expand(7.0, 7.0, 7.0)) {
                    (it is EntityLivingBase && it.isNonBoss) || (it is IProjectile)
                }
                pushEntities(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, 7.0, if (state.getValue(SAND_TYPE) == EnumSandType.STORM) 0.1 else -0.1, entities)
            } else if (state.getValue(SAND_TYPE) == EnumSandType.PAIN) {
                val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB(pos).expand(1/16.0, 1/16.0, 1/16.0))
                for (entity in entities)
                    entity.attackEntityFrom(DamageSource.cactus, 1f)

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
                    entity.motionX += velocity * vec.xCoord
                    entity.motionY += velocity * vec.yCoord
                    entity.motionZ += velocity * vec.zCoord
                    entity.fallDistance = 0f
                }
            }
        }

    }
}
