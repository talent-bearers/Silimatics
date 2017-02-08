package wiresegal.silimatics.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockModContainer
import com.teamwizardry.librarianlib.common.base.block.TileMod
import com.teamwizardry.librarianlib.common.util.autoregister.TileRegister
import net.minecraft.block.BlockPlanks
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyEnum
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumParticleTypes
import net.minecraft.util.math.AxisAlignedBB
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraft.world.WorldServer
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import wiresegal.silimatics.common.core.ModCreativeTab
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.item.EnumSandType.Companion.capitalizeFirst

/**
 * Created by Elad on 8/4/2016.
 */
class BlockSifter(name: String) : BlockModContainer(name, Material.WOOD, *getVariants(name)) {
    override fun createTileEntity(world: World, state: IBlockState): TileEntity? {
        return TileBlockSifter()
    }

    companion object {

        fun getVariants(name: String) = Array(BlockPlanks.EnumType.values().size) {
            name + BlockPlanks.EnumType.values()[it].name.toLowerCase().split("_").joinToString("", transform = { it.capitalizeFirst() })
        }

        val choices = arrayListOf<Pair<Double, ItemStack>>()
        val remainder = arrayListOf<ItemStack>()

        init {
            for (i in EnumSandType.values()) {
                if (i.chance == null)
                    remainder.add(ItemStack(ModItems.sand, 1, i.ordinal))
                else
                    choices.add(i.chance to ItemStack(ModItems.sand, 1, i.ordinal))
            }
        }

        val PROP_TYPE = PropertyEnum.create("type", BlockPlanks.EnumType::class.java)

        val AABB = AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 11 / 16.0, 1.0)

        fun dropSand(worldIn: World, pos: BlockPos) {
            val stacks = arrayListOf<ItemStack>()

            for (i in 0..2) {
                var rand = worldIn.rand.nextDouble()

                for (choice in choices)
                    if (rand < choice.first) {
                        stacks.add(choice.second.copy())
                        rand = 1.0
                        break
                    } else
                        rand -= choice.first
                if (rand < 1.0)
                    stacks.add(remainder[worldIn.rand.nextInt(remainder.size)].copy())
            }

            val blockBelow = !worldIn.isAirBlock(pos.down())

            if (blockBelow) {
                val tileBelow = worldIn.getTileEntity(pos.down())
                if (tileBelow != null && tileBelow.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
                    val handler = tileBelow.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)

                    for (i in stacks.toList()) {
                        val newStack = ItemHandlerHelper.insertItem(handler, i, true)
                        if (newStack == null || newStack.stackSize == 0) {
                            ItemHandlerHelper.insertItem(handler, i.copy(), false)
                            stacks.remove(i)
                        }
                    }
                }
            }

            for (stack in stacks) {
                val entityitem = EntityItem(worldIn, pos.x + 0.5, pos.y + if (blockBelow) 0.75 else -0.625, pos.z + 0.5, stack)
                entityitem.motionX = worldIn.rand.nextGaussian() * 0.05
                entityitem.motionY = 0.2
                entityitem.motionZ = worldIn.rand.nextGaussian() * 0.05
                worldIn.spawnEntityInWorld(entityitem)
            }

            if (worldIn is WorldServer)
                worldIn.spawnParticle(EnumParticleTypes.FALLING_DUST, pos.x + 0.5, pos.y + 0.625, pos.z + 0.5, 12, 0.18, 0.0, 0.18, 0.0, 12)
        }
    }

    init {
        setHardness(2.0F)
        setResistance(5.0F)
        soundType = SoundType.WOOD
        ModCreativeTab.set(this)
    }

    @SideOnly(Side.CLIENT)
    override fun getBlockLayer(): BlockRenderLayer {
        return BlockRenderLayer.CUTOUT
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, PROP_TYPE)
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(PROP_TYPE, BlockPlanks.EnumType.byMetadata(meta))
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return state.getValue(PROP_TYPE).metadata
    }

    override fun damageDropped(state: IBlockState): Int {
        return getMetaFromState(state)
    }

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    override fun getBoundingBox(state: IBlockState?, source: IBlockAccess?, pos: BlockPos?): AxisAlignedBB {
        return AABB
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer?, hand: EnumHand, heldItem: ItemStack?, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (heldItem != null && heldItem.item == Item.getItemFromBlock(Blocks.SAND) && !worldIn.isRemote) {
            if (playerIn?.capabilities?.isCreativeMode?.not() ?: true) {
                heldItem.stackSize--
                if (heldItem.stackSize <= 0) playerIn?.inventory?.deleteStack(heldItem)
            }
            dropSand(worldIn, pos)
        }
        return true
    }


    @TileRegister("silisifter")
    class TileBlockSifter : TileMod() {
        val inventoryFake = object : IItemHandler {
            override fun getStackInSlot(slot: Int): ItemStack? {
                return null
            }

            override fun insertItem(slot: Int, stack: ItemStack?, simulate: Boolean): ItemStack? {
                if (stack == null || stack.item != Item.getItemFromBlock(Blocks.SAND)) return stack
                val ret = stack.copy()
                ret.stackSize--
                val worldObj = world
                if (!simulate && !worldObj.isRemote) dropSand(world, pos)
                return if (ret.stackSize == 0) null else ret
            }

            override fun getSlots(): Int {
                return 1
            }

            override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack? {
                return null
            }
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.DOWN)
                return inventoryFake as T
            return super.getCapability(capability, facing)
        }

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing != EnumFacing.DOWN)
                return true
            return super.hasCapability(capability, facing)
        }
    }
}

