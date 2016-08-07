package wiresegal.silimatics.common.block

import net.minecraft.block.Block
import net.minecraft.block.SoundType
import net.minecraft.block.material.Material
import net.minecraft.block.properties.IProperty
import net.minecraft.block.properties.PropertyBool
import net.minecraft.block.state.BlockStateContainer
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.InventoryHelper
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.items.CapabilityItemHandler
import net.minecraftforge.items.IItemHandler
import net.minecraftforge.items.ItemHandlerHelper
import net.minecraftforge.items.ItemStackHandler
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModCreativeTab
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.util.SlotWrapperHandler
import wiresegal.zenmodelloader.common.block.base.BlockModContainer

/**
 * Created by Elad on 8/5/2016.
 */
class BlockLensGrinder(name: String) : BlockModContainer(name, Material.IRON) {

    companion object {
        val PROP_ON = PropertyBool.create("on")
    }

    init {
        setHardness(1.5F)
        setResistance(10.0F)
        soundType = SoundType.STONE

        ModCreativeTab.set(this)
    }

    override fun createBlockState(): BlockStateContainer {
        return BlockStateContainer(this, PROP_ON)
    }

    override fun getMetaFromState(state: IBlockState): Int {
        return if (state.getValue(PROP_ON)) 1 else 0
    }

    override fun getStateFromMeta(meta: Int): IBlockState {
        return defaultState.withProperty(PROP_ON, meta != 0)
    }

    override fun breakBlock(worldIn: World, pos: BlockPos, state: IBlockState) {
        val tile = (worldIn.getTileEntity(pos) as TileLensGrinder)
        for (i in 0..tile.inventory.slots - 1) {
            val stack = tile.inventory.getStackInSlot(i)
            stack.tagCompound = null
            InventoryHelper.spawnItemStack(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack)
        }
        super.breakBlock(worldIn, pos, state)
    }

    override fun onBlockActivated(world: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand?, heldItem: ItemStack?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        return (world.getTileEntity(pos) as TileLensGrinder).onBlockActivated(playerIn, heldItem)
    }

    override val ignoredProperties: Array<IProperty<*>>?
        get() = arrayOf(PROP_ON)

    override fun createNewTileEntity(worldIn: World, meta: Int) = TileLensGrinder()

    override fun neighborChanged(state: IBlockState, worldIn: World, pos: BlockPos, blockIn: Block) {
        val on = worldIn.isBlockPowered(pos)
        if (on && !state.getValue(PROP_ON))
            (worldIn.getTileEntity(pos) as TileLensGrinder).tickDown()
        val newState = state.withProperty(PROP_ON, on)
        if (newState != state)
            worldIn.setBlockState(pos, newState)

    }

    override fun getComparatorInputOverride(blockState: IBlockState?, worldIn: World, pos: BlockPos): Int {
        val tile = (worldIn.getTileEntity(pos) as TileLensGrinder)
        var items = 0
        for (i in 0..tile.inventory.slots-1)
            if (tile.inventory.getStackInSlot(i) != null)
                items++
        return (items / 11.0 * 15).toInt()
    }

    override fun hasComparatorInputOverride(state: IBlockState?): Boolean {
        return true
    }

    override fun isFullCube(state: IBlockState?) = false

    override fun isOpaqueCube(state: IBlockState?) = false

    class TileLensGrinder : TileMod() {

        var inventory = object : ItemStackHandler(11) {

            var flipCheck = false

            override fun insertItem(slot: Int, stack: ItemStack?, simulate: Boolean): ItemStack? {
                if (stack?.item == Item.getItemFromBlock(ModBlocks.glassPane)) {
                    flipCheck = true
                    val ret = super.insertItem(slot, stack, simulate)
                    flipCheck = false
                    return ret
                }
                return stack
            }

            override fun onContentsChanged(slot: Int) {
                if (flipCheck && slot == 0) {
                    ItemNBTHelper.setBoolean(getStackInSlot(slot) ?: return, TAG_SHOULD_FLIP, flipped)
                    flipped = !flipped
                }
                world.updateComparatorOutputLevel(pos, world.getBlockState(pos).block)
            }

            override fun getStackLimit(slot: Int, stack: ItemStack?): Int {
                return 1
            }
        }

        var flipped = true

        var topInventory = SlotWrapperHandler(inventory, 0..0, 0)
        var bottomInventory = object : SlotWrapperHandler(inventory, (inventory.slots - 1)..(inventory.slots - 1), inventory.slots - 1) {
            override fun getStackInSlot(slot: Int): ItemStack? {
                return ItemStack(ModItems.lens, 1, super.getStackInSlot(slot)?.metadata ?: return null)
            }

            override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack? {
                return ItemStack(ModItems.lens, 1, super.extractItem(slot, amount, simulate)?.metadata ?: return null)
            }
        }

        fun tickDown(): Boolean {
            var flag = false
            for (i in (inventory.slots - 1) downTo 0) {
                if (i + 1 < inventory.slots) {
                    val stack = inventory.getStackInSlot(i)?.copy() ?: continue
                    inventory.setStackInSlot(i, null)
                    inventory.setStackInSlot(i + 1, stack)
                    flag = true
                } else {
                    val stack = inventory.getStackInSlot(i)?.copy() ?: continue
                    if (!world.isRemote) {
                        val lens = ItemStack(ModItems.lens, 1, stack.metadata)
                        var dropStack = true
                        val blockBelow = !world.isAirBlock(pos.down())

                        if (blockBelow) {
                            val tileBelow = world.getTileEntity(pos.down())
                            if (tileBelow != null && tileBelow.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) {
                                val handler = tileBelow.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)

                                val newStack = ItemHandlerHelper.insertItem(handler, lens, true)
                                if (newStack == null || newStack.stackSize == 0) {
                                    ItemHandlerHelper.insertItem(handler, lens, false)
                                    dropStack = false
                                }
                            }
                        }

                        if (dropStack) {
                            val entityitem = EntityItem(world, pos.x + 0.5, pos.y + if (blockBelow) 1.0 else -0.625, pos.z + 0.5, lens)
                            entityitem.motionX = 0.0
                            entityitem.motionY = if (blockBelow) 0.15 else 0.0
                            entityitem.motionZ = 0.0
                            world.spawnEntityInWorld(entityitem)
                        }
                    }
                    inventory.setStackInSlot(i, null)
                    flag = true
                }
            }
            return flag
        }

        fun onBlockActivated(playerIn: EntityPlayer, heldItem: ItemStack?): Boolean {
            var flag = false

            if (inventory.getStackInSlot(0) == null && heldItem != null && heldItem.item == Item.getItemFromBlock(ModBlocks.glassPane)) {
                if (!world.isRemote) {
                    if (!playerIn.capabilities.isCreativeMode) {
                        heldItem.stackSize--
                        if (heldItem.stackSize == 0)
                            playerIn.inventory.deleteStack(heldItem)
                    }

                    val stack = heldItem.copy()
                    ItemNBTHelper.setBoolean(stack, TAG_SHOULD_FLIP, flipped)
                    stack.stackSize = 1
                    inventory.setStackInSlot(0, stack)
                    flipped = !flipped
                }
                flag = true
            }

            if (!flag) flag = tickDown()

            if (flag)
                markDirty()
            return flag
        }

        @Suppress("UNCHECKED_CAST")
        override fun <T : Any?> getCapability(capability: Capability<T>, facing: EnumFacing?): T {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
                if (facing == null)
                    return inventory as T
                else if (facing == EnumFacing.UP)
                    return topInventory as T
                else if (facing == EnumFacing.DOWN)
                    return bottomInventory as T
            }
            return super.getCapability(capability, facing)
        }

        override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
            if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                if (facing == null || facing.axis == EnumFacing.Axis.Y) return true
            return super.hasCapability(capability, facing)
        }

        override fun writeCustomNBT(cmp: NBTTagCompound) {
            cmp.setTag("inv", inventory.serializeNBT())
            cmp.setBoolean("flip", flipped)
        }

        override fun readCustomNBT(cmp: NBTTagCompound) {
            val inv = cmp.getCompoundTag("inv")
            if (inv != null)
                inventory.deserializeNBT(inv)
            flipped = cmp.getBoolean("flip")
        }

        companion object {
            val TAG_SHOULD_FLIP = "flip"
        }

    }

}
