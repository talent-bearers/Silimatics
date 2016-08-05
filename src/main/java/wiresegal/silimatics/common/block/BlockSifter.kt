package wiresegal.silimatics.common.block

import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import wiresegal.silimatics.common.Silimatics
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.zenmodelloader.common.block.base.BlockMod
import wiresegal.zenmodelloader.common.lib.LibMisc

/**
 * Created by Elad on 8/4/2016.
 */
class BlockSifter(name: String) : BlockMod(name, Material.WOOD) {

    companion object {
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
    }

    val lootTable: ResourceLocation? = ResourceLocation(LibMisc.MOD_ID, "loottables/sifter")

    operator fun invoke() : ItemStack {
        return ItemStack(this)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer?, hand: EnumHand, heldItem: ItemStack?, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (heldItem != null && heldItem.item == Item.getItemFromBlock(Blocks.SAND) && !worldIn.isRemote) {
            if (playerIn?.capabilities?.isCreativeMode?.not() ?: true) {
                heldItem.stackSize--
                if (heldItem.stackSize <= 0) playerIn?.inventory?.deleteStack(heldItem)
            }

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

            for (stack in stacks) {
                val entityitem = EntityItem(worldIn, pos.x + 0.5, pos.y + 1.5, pos.z + 0.5, stack)
                entityitem.motionX = worldIn.rand.nextGaussian() * 0.05
                entityitem.motionY = 0.2
                entityitem.motionZ = worldIn.rand.nextGaussian() * 0.05
                worldIn.spawnEntityInWorld(entityitem)
            }

            Silimatics.proxy.makeParticleDust(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 0.0, -1.0, 0.0, 12)

        }
        return true
    }
}

