package wiresegal.silimatics.common.block

import com.teamwizardry.librarianlib.common.base.block.BlockMod
import com.teamwizardry.librarianlib.common.base.block.TileMod
import com.teamwizardry.librarianlib.common.base.item.ItemMod
import com.teamwizardry.librarianlib.common.util.autoregister.TileRegister
import com.teamwizardry.librarianlib.common.util.saving.Save
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.lib.LibNames
import wiresegal.silimatics.common.util.Vec2f
import wiresegal.silimatics.common.util.map

/**
 * Created by Elad on 10/12/2016.
 */
class BlockCymaticPlate : BlockMod(LibNames.CYMATIC_PLATE, Material.GLASS), ITileEntityProvider {
    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        return TileEntityCymaticPlate()
    }

    init {
        @Suppress("DEPRECATION")
        setHardness(Blocks.GLASS.getBlockHardness(null, null, null))

    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer?, hand: EnumHand?, heldItem: ItemStack?, side: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (heldItem != null && heldItem.item == Item.getItemFromBlock(Blocks.GLASS)) {
            val te = worldIn.getTileEntity(pos) as TileEntityCymaticPlate
            if(te.hasGlass || te.item != null) return false
            te.hasGlass = true
            if (heldItem.stackSize > 1) heldItem.stackSize-- else playerIn?.inventory?.deleteStack(heldItem)
            te.markDirty()
            return true
        } else if(heldItem != null && heldItem.item == ModItems.fork) {
            val te = worldIn.getTileEntity(pos) as TileEntityCymaticPlate
            if(!te.hasGlass || te.item != null) return false
            te.lastHit = Vec2f(round(hitX).toFloat(), round(hitZ).toFloat())
            //println(te.lastHit)
            playerIn?.addChatComponentMessage(TextComponentString(Vec2f(hitX, hitZ).round().toString()))

            te.item = (CymaticPlateRecipes.getValueByVec2f(te.lastHit!!) ?: return false).result
            //te.item = CymaticPlateRecipes.getValue(ResourceLocation("smedry", "test"))?.result?.copy()
            te.hasGlass = false
            te.markDirty()
            heldItem.damageItem(1, playerIn)
            return true
        } else if(heldItem == null) {
            val te = worldIn.getTileEntity(pos) as TileEntityCymaticPlate
            if(te.item != null) {
                playerIn?.inventory?.addItemStackToInventory(te.item)
                te.item = null
            }


            te.markDirty()
            return false
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ)
    }
    companion object {
        fun round(num: Float): Int {
            /*val df = DecimalFormat("#.#");
            df.roundingMode = RoundingMode.CEILING;
            return (java.lang.Float.valueOf(df.format(num)) - Math.floor(java.lang.Float.valueOf(df.format(num)).toDouble())).toFloat()*/
            return (0.5 + num * 10).toInt().map(0, 10, 0, 3)
        }
    }
    @TileRegister("siliplate")
    class TileEntityCymaticPlate : TileMod() {
        @Save var hasGlass = false
        var lastHit: Vec2f? = null
        @Save var item: ItemStack? = null
        override fun readCustomNBT(cmp: NBTTagCompound) {
            if(cmp.hasKey("xf") && cmp.hasKey("zf"))
                lastHit = Vec2f(cmp.getFloat("xf"), cmp.getFloat("zf"))
        }

        override fun writeCustomNBT(cmp: NBTTagCompound, sync: Boolean) {
            if(lastHit != null) {
                cmp.setFloat("xf", (lastHit as Vec2f).x)
                cmp.setFloat("zf", (lastHit as Vec2f).y)
            }
        }

    }
    class ItemTuningFork : ItemMod(LibNames.FORK) {
        init {
            maxDamage = 20
            maxStackSize = 1
        }
    }
    data class CymaticPlateRecipe(var vec2F: Vec2f, private val resultStack: ItemStack) {
        val result: ItemStack get() = resultStack.copy()

        fun round(): CymaticPlateRecipe {
            vec2F = vec2F.round()
            return this
        }

    }
    object CymaticPlateRecipes {
        val recipes = mutableListOf<CymaticPlateRecipe>()

        fun register(value: CymaticPlateRecipe) {
            recipes.add(value)
        }

        operator fun contains(key: CymaticPlateRecipe?): Boolean
            = key != null && recipes.contains(key)

        operator fun iterator(): MutableIterator<CymaticPlateRecipe> {
            return object : MutableIterator<CymaticPlateRecipe> {
                var index: Int = 0
                override fun hasNext(): Boolean
                    = recipes.size > index


                override fun next(): CymaticPlateRecipe
                    = recipes[index++]


                override fun remove() {
                    recipes.remove(recipes[index])
                }


            }
        }

        fun getValueByVec2f(vec2F: Vec2f): CymaticPlateRecipe? {
            for(recipe in this) if(recipe.vec2F == vec2F) return recipe
            return null
        }

    }
}
