package wiresegal.silimatics.common.block

import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.statemap.IStateMapper
import net.minecraft.client.renderer.block.statemap.StateMap
import net.minecraft.client.renderer.color.IBlockColor
import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.IProjectile
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.MobEffects
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.DamageSource
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.util.BrightsandPower
import wiresegal.zenmodelloader.common.core.IBlockColorProvider
import java.util.*

/**
 * @author WireSegal
 * Created at 10:53 PM on 8/4/16.
 */
class BlockGlassPane(name: String) : BlockModPane(name, Material.GLASS, true, *EnumSandType.getSandTypeNamesFor(name)), IBlockColorProvider {

    @SideOnly(Side.CLIENT)
    override fun getBlockColor(): IBlockColor {
        return IBlockColor { iBlockState, iBlockAccess, blockPos, i -> iBlockState.getValue(BlockGlass.SAND_TYPE).glassColor }
    }

    @SideOnly(Side.CLIENT)
    override fun getItemColor(): IItemColor {
        return IItemColor { itemStack, i -> EnumSandType.values()[itemStack.itemDamage % EnumSandType.values().size].glassColor }
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        super.breakBlock(worldIn, pos, state)
        worldIn.removeTileEntity(pos)
    }

    override fun eventReceived(state: IBlockState?, worldIn: World, pos: BlockPos?, eventID: Int, eventParam: Int): Boolean {
        val tileentity = worldIn.getTileEntity(pos)
        return if (tileentity == null) false else tileentity.receiveClientEvent(eventID, eventParam)
    }

    override fun damageDropped(state: IBlockState): Int {
        return getMetaFromState(state)
    }

    override fun getLightValue(state: IBlockState, world: IBlockAccess?, pos: BlockPos?): Int {
        return if (state.getValue(BlockGlass.SAND_TYPE) == EnumSandType.BRIGHT) 15 else 0
    }

    init {
        blockHardness = 0.3F
        soundType = SoundType.GLASS
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, NORTH, EAST, WEST, SOUTH, BlockGlass.SAND_TYPE)
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.TRANSLUCENT
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(BlockGlass.SAND_TYPE).ordinal
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(BlockGlass.SAND_TYPE, EnumSandType.values()[meta % EnumSandType.values().size])
    }

    override fun canPaneConnectTo(world: IBlockAccess, pos: BlockPos, dir: EnumFacing): Boolean {
        return super.canPaneConnectTo(world, pos, dir) || world.getBlockState(pos.offset(dir))?.block == ModBlocks.glass
    }

    override fun getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): MutableList<ItemStack>? {
        return if(state.getValue(BlockGlass.SAND_TYPE) == EnumSandType.VIEW) mutableListOf(ModItems.shard.getCommunicatorShardStack()) else super.getDrops(world, pos, state, fortune)
    }

    @SideOnly(Side.CLIENT)
    override fun getStateMapper(): IStateMapper {
        return StateMap.Builder()
                .withName(BlockGlass.SAND_TYPE)
                .withSuffix("GlassPane").build()
    }
    override fun updateTick(worldObj: World, pos: BlockPos, bs: IBlockState, rand: Random?) {
        val state = worldObj.getBlockState(pos)
        if (!BrightsandPower.hasBrightsandPower(worldObj, pos) && state.getValue(BlockGlass.SAND_TYPE) != EnumSandType.HEART && state.getValue(BlockGlass.SAND_TYPE) != EnumSandType.TRAIL) return
        when (state.getValue(BlockGlass.SAND_TYPE)) {
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
                BlockGlass.pushEntities(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, range, if (state.getValue(BlockGlass.SAND_TYPE) == EnumSandType.STORM) 0.05 else -0.05, entities)
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
                    true //todo it.isOculator()
                            && state.block == ModBlocks.glass
                }.forEach {
                    worldObj.setBlockState(pos, ModBlocks.brokenGlass.defaultState)
                    (worldObj.getTileEntity(pos) as BlockBrokenGlass.TileEntityBrokenGlass).ticks = 0
                }

            }
            EnumSandType.HEAT -> {
                if (!worldObj.isRemote && BrightsandPower.hasBrightsandPowerAndRedstonePower(worldObj, pos))
                    worldObj.createExplosion(null, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 8f, true)

            }

            EnumSandType.SUN -> {
                for (player in worldObj.playerEntities) {
                    val blockState = BlockGlass.getBlockLookedAt(player, worldObj = worldObj)
                    if (blockState != null && (blockState.block == ModBlocks.glassPane || blockState.block == ModBlocks.glass) && blockState.getValue(BlockGlass.SAND_TYPE) == EnumSandType.SUN) player.addPotionEffect(PotionEffect(MobEffects.BLINDNESS, 100, 0))
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
                for (pos0 in BlockPos.getAllInBox(pos.add(-3, -3, -3), pos.add(4, 4, 4))) if (worldObj.getTileEntity(pos0) is ITickable) (worldObj.getTileEntity(pos0) as ITickable).update()
            }

            null -> {
                //NOOP
            }

        }


        worldObj.scheduleUpdate(pos, this, 0)
    }

    override fun requiresUpdates(): Boolean {
        return true
    }

    override fun onBlockAdded(worldIn: World, pos: BlockPos?, state: IBlockState?) {
        worldIn.scheduleUpdate(pos, this, 0)
    }
}
