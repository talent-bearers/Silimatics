package wiresegal.silimatics.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockModContainer
import com.teamwizardry.librarianlib.common.base.block.IBlockColorProvider
import com.teamwizardry.librarianlib.common.base.block.TileMod
import com.teamwizardry.librarianlib.common.util.ConfigPropertyBoolean
import com.teamwizardry.librarianlib.common.util.autoregister.TileRegister
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.item.EntityFallingBlock
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModCreativeTab
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.lens.LensOculator
import wiresegal.silimatics.common.util.BrightsandPower
import wiresegal.silimatics.common.util.SilimaticEvents
import wiresegal.silimatics.common.util.hasRedstoneSignalSimple
import java.awt.Color

/**
 * @author WireSegal
 * Created at 9:30 AM on 8/4/16.
 */
@Optional.Interface(iface = "com.teamwizardry.refraction.api.IBeamHandler", modid = "refraction")
class BlockGlass(name: String) : BlockModContainer(name, Material.GLASS, *EnumSandType.getSandTypeNamesFor(name)), IBlockColorProvider {

    fun getColor(state: IBlockState): Color {
       /* val colour = Color(Color.HSBtoRGB(world.totalWorldTime * 0.005f, 1f, 1f) / 2)
        return if(colour == Color.CYAN) Color.WHITE else colour*/
        return Color(state.getValue(BlockGlass.SAND_TYPE).glassColor)
    }

    companion object {
        @ConfigPropertyBoolean(category = "Compat", comment = "Enable the Silimatics-Refraction compat features", defaultValue = true, id = "silifraction", modid = "smedry")
        @JvmStatic var enabled = true
        val SAND_TYPE = PropertyEnum.create("sand", EnumSandType::class.java)
        fun getBlockLookedAt(e: Entity, maxDistance: Double = 32.0, worldObj: World): IBlockState? {
            val pos = LensOculator.raycast(e, maxDistance)
            return if (pos != null && pos.typeOfHit == RayTraceResult.Type.BLOCK) worldObj.getBlockState(pos.blockPos) else null
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
                    entity.velocityChanged = true
                    if (velocity * vec.yCoord > 0) entity.fallDistance = 0f
                }
            }
        }
    }

    init {
        setHardness(0.3F)
        soundType = SoundType.GLASS
        ModCreativeTab.set(this)

    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, SAND_TYPE)
    }
    override fun getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): MutableList<ItemStack>? {
        return if(state.getValue(BlockGlass.SAND_TYPE) == EnumSandType.VIEW) mutableListOf(ModItems.shard.getCommunicatorShardStack()) else super.getDrops(world, pos, state, fortune)
    }

    override val itemColorFunction: ((ItemStack, Int) -> Int)?
        get() =  { itemStack, i -> EnumSandType.values()[itemStack.itemDamage % EnumSandType.values().size].glassColor }
    override val blockColorFunction: ((IBlockState, IBlockAccess?, BlockPos?, Int) -> Int)?
        get() = { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(SAND_TYPE).glassColor }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(SAND_TYPE).ordinal
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(SAND_TYPE, EnumSandType.values()[meta % EnumSandType.values().size])
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess?, pos: BlockPos?): Int {
        return if (state.getValue(SAND_TYPE) == EnumSandType.BRIGHT) 15 else 0
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

    override fun shouldCheckWeakPower(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, side: EnumFacing?): Boolean {
        return true
    }

    @SideOnly(Side.CLIENT)
    override fun shouldSideBeRendered(blockState: IBlockState, blockAccess: IBlockAccess, pos: BlockPos, side: EnumFacing): Boolean {
        val iblockstate = blockAccess.getBlockState(pos.offset(side))
        val block = iblockstate.block

        @Suppress("DEPRECATION")
        return if (blockState !== iblockstate) true
        else if (block === this) false
        else super.shouldSideBeRendered(blockState, blockAccess, pos, side)
    }

    override fun onNeighborChange(world: IBlockAccess?, pos: BlockPos?, neighbor: BlockPos?) {
        (world as? World)?.scheduleUpdate(pos, this, 0)
    }

    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return SmedryGlassTile()
    }

    @TileRegister("oooshiny")
    class SmedryGlassTile : TileMod(), ITickable {
        override fun update() {
            val state = worldObj.getBlockState(pos)
            val entitiesAround = worldObj.getEntitiesWithinAABB(EntityFallingBlock::class.java, AxisAlignedBB(pos.add(-1, 0, -1), pos.add(2, 0, 2)))
            var flag: Boolean = false
            entitiesAround.filter { it.block?.block == ModBlocks.sand && it.block?.getValue(BlockSand.SAND_TYPE) == EnumSandType.BRIGHT }.forEach { flag = true }
            EnumFacing.values().forEach { if(SilimaticEvents.tracker.map { it.toLong() }.contains(pos.add(it.directionVec).toLong())) flag = true }
            val expectedState = BrightsandPower.hasBrightsandPower(worldObj, pos) || flag
            if (!expectedState && state.getValue(SAND_TYPE) != EnumSandType.HEART && state.getValue(SAND_TYPE) != EnumSandType.TRAIL) return
            when (state.getValue(SAND_TYPE)) {
                EnumSandType.BLOOD -> {
                    val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, state.getBoundingBox(worldObj, pos).offset(pos.up()))
                    if (!worldObj.isRemote) for (entity in entities) {
                        entity.addPotionEffect(PotionEffect(MobEffects.SLOWNESS, 5, 3))
                        entity.addPotionEffect(PotionEffect(MobEffects.STRENGTH, 5))
                        entity.addPotionEffect(PotionEffect(MobEffects.RESISTANCE, 5))
                    }
                }
                EnumSandType.STORM, EnumSandType.VOID -> {
                    val range = 4.0
                    val entities = worldObj.getEntitiesWithinAABB(Entity::class.java,
                            state.getBoundingBox(worldObj, pos).offset(pos).expand(range, range, range)) {
                        (it is EntityLivingBase && it.isNonBoss) || (it is IProjectile) || (it is EntityItem)
                    }
                    pushEntities(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, range, if (state.getValue(SAND_TYPE) == EnumSandType.STORM) 0.05 else -0.05, entities)
                }
                EnumSandType.PAIN -> {
                    val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, state.getBoundingBox(worldObj, pos).offset(pos).expand(1 / 16.0, 1 / 16.0, 1 / 16.0))
                    for (entity in entities)
                        entity.attackEntityFrom(DamageSource.cactus, 1f)
                }
                EnumSandType.TRAIL -> {
                    val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, state.getBoundingBox(worldObj, pos).offset(pos).expand(0.125, 1 / 32.0, 0.125).offset(0.0, -1 / 32.0, 0.0))
                    for (entity in entities) {
                        if (entity.getItemStackFromSlot(EntityEquipmentSlot.FEET)?.item == ModItems.boots && (entity !is EntityPlayer || !entity.capabilities.isFlying)) {
                            if (entity is EntityPlayer && entity.isSneaking) entity.motionY = 0.0
                            else if (entity.rotationPitch > 80) entity.motionY = -0.2
                            else entity.motionY = 0.2
                            entity.fallDistance = 0F
                            entity.velocityChanged = true
                        }
                    }
                }
                EnumSandType.HEART -> {
                    /* val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, state.getBoundingBox(worldObj, pos).offset(pos).expand(0.0, 2.0, 0.0).offset(0.0, 1.0, 0.0))
                     entities.filter {
                         it.getActivePotionEffect(MobEffects.REGENERATION) == null
                     }.forEach {
                         it.addPotionEffect(PotionEffect(MobEffects.REGENERATION, 80, 1))
                     }*/
                    val entities = worldObj.getEntitiesWithinAABB(EntityPlayer::class.java, state.getBoundingBox(worldObj, pos).offset(pos).expand(2.0, 2.0, 2.0).offset(1.5, 1.5, 1.5))
                    entities.filter {
                        @Suppress("SimplifyBooleanWithConstants")
                        (true //todo it.isOculator()
                                && state.block == ModBlocks.glass)
                    }.forEach {
                        worldObj.setBlockState(pos, ModBlocks.brokenGlass.defaultState)
                        (worldObj.getTileEntity(pos) as BlockBrokenGlass.TileEntityBrokenGlass).ticks = 0
                    }

                }
                EnumSandType.HEAT -> {
                    if (!worldObj.isRemote && worldObj.hasRedstoneSignalSimple(pos))
                        worldObj.createExplosion(null, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 8f, true)

                }

                EnumSandType.SUN -> {
                    for (player in worldObj.playerEntities) {
                        val blockState = getBlockLookedAt(player, worldObj = worldObj)
                        if (blockState != null && (blockState.block == ModBlocks.glassPane || blockState.block == ModBlocks.glass) && blockState.getValue(SAND_TYPE) == EnumSandType.SUN) player.addPotionEffect(PotionEffect(MobEffects.BLINDNESS, 100, 0))
                    }
                }

                EnumSandType.DULL -> {
                    //NOOP
                }

                EnumSandType.BRIGHT -> {
                    //NOOP
                }

                EnumSandType.RASHID -> {
                    //NOOP
                }

                EnumSandType.VIEW -> {
                    //NOOP
                }

                EnumSandType.SCHOOL -> {
                    for (pos0 in BlockPos.getAllInBox(pos.add(-3, -3, -3), pos.add(4, 4, 4))) if (worldObj.getTileEntity(pos0) is ITickable && worldObj.getTileEntity(pos0) !is SmedryGlassTile) (worldObj.getTileEntity(pos0) as ITickable).update()
                }

                null -> {
                    //NOOP
                }

                EnumSandType.LINKER -> {
                    /* todo transporter's glass */
                }
            }
        }


    }
}
