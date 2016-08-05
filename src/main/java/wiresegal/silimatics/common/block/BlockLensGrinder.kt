package wiresegal.silimatics.common.block

import com.google.common.collect.Lists
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.BlockRenderLayer
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import wiresegal.silimatics.common.core.ModBlocks
import wiresegal.silimatics.common.core.ModItems
import wiresegal.zenmodelloader.common.block.base.BlockModContainer

/**
 * Created by Elad on 8/5/2016.
 */
class BlockLensGrinder(name: String) : BlockModContainer(name, Material.IRON) {

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer?, hand: EnumHand?, heldItem: ItemStack?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        return (worldIn.getTileEntity(pos) as TileLensGrinder).onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ)
    }

    override fun createNewTileEntity(worldIn: World, meta: Int): TileEntity {
        return TileLensGrinder()
    }

    override fun canRenderInLayer(state: IBlockState?, layer: BlockRenderLayer?): Boolean {
        return layer == BlockRenderLayer.TRANSLUCENT
    }

    override fun isFullCube(state: IBlockState?): Boolean {
        return false
    }

    override fun isOpaqueCube(state: IBlockState?): Boolean {
        return false
    }

    class TileLensGrinder : TileMod() {
        val inventory = Lists.newArrayList<ItemStack>()


        fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer?, hand: EnumHand?, heldItem: ItemStack?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
            println(inventory.size)
            if(inventory.size >= 1) {
                if(!worldIn.isRemote) {
                    val entityitem = EntityItem(worldIn, pos.x + 0.5, pos.y + 1.5, pos.z + 0.5, ItemStack(ModItems.lens, 1, inventory[0].metadata));
                    entityitem.motionX = worldIn.rand.nextGaussian() * 0.05
                    entityitem.motionY = 0.2
                    entityitem.motionZ = worldIn.rand.nextGaussian() * 0.05
                    worldIn.spawnEntityInWorld(entityitem)
                    inventory.clear()
                }
                return true
            }
            else if (heldItem != null && heldItem.item == Item.getItemFromBlock(ModBlocks.glassPane) && !worldIn.isRemote) {
                if (playerIn?.capabilities?.isCreativeMode?.not() ?: true) {
                    heldItem.stackSize--
                    if (heldItem.stackSize <= 0) playerIn?.inventory?.deleteStack(heldItem)
                }
                inventory.add(heldItem)
            }
            return true;
        }

    }

}
