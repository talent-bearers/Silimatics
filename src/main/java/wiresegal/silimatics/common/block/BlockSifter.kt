package wiresegal.silimatics.common.block

import com.google.common.collect.Lists
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
import wiresegal.zenmodelloader.common.block.base.BlockMod
import wiresegal.zenmodelloader.common.lib.LibMisc
import java.util.concurrent.ThreadLocalRandom

/**
 * Created by Elad on 8/4/2016.
 */
class BlockSifter(name: String) : BlockMod(name, Material.WOOD) {
    val lootTable: ResourceLocation? = ResourceLocation(LibMisc.MOD_ID, "loottables/sifter")

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer?, hand: EnumHand, heldItem: ItemStack?, side: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (heldItem != null && heldItem.item == Item.getItemFromBlock(Blocks.SAND) && !worldIn.isRemote) {
            if(playerIn?.capabilities?.isCreativeMode?.not() ?: true) {
                heldItem.stackSize--
                if (heldItem.stackSize <= 0) playerIn?.inventory?.deleteStack(heldItem)
            }
            val stacks = Lists.newArrayList<ItemStack>()
            for(i in 1..4) {
                val randomInt = ThreadLocalRandom.current().nextInt(1001)
                if (randomInt < 750) stacks.add(ItemStack(ModItems.sand, 1, 0))
                else if (randomInt < 900) stacks.add(ItemStack(ModItems.sand, 1, 2))
                else if (randomInt < 901) stacks.add(ItemStack(ModItems.sand, 1, 1))
                else stacks.add(ItemStack(ModItems.sand, 1, ThreadLocalRandom.current().nextInt(3, 11)))
            }
            for(stack in stacks) {
                //worldIn.spawnEntityInWorld(EntityItem(worldIn, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), stack))
                val entityitem = EntityItem(worldIn, pos.x + 0.5, pos.y + 1.5, pos.z + 0.5, stack);
                entityitem.motionX = worldIn.rand.nextGaussian() * 0.05
                entityitem.motionY = 0.2
                entityitem.motionZ = worldIn.rand.nextGaussian() * 0.05
                worldIn.spawnEntityInWorld(entityitem)
            }

            Silimatics.proxy!!.makeParticleDust(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), 0.0, -1.0, 0.0, 12)

        }
        return true
    }
}

