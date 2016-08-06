package wiresegal.silimatics.common.core

import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import wiresegal.silimatics.common.lib.LibMisc
import java.util.*

object ModCreativeTab : CreativeTabs(LibMisc.MODID) {
    internal lateinit var list: MutableList<ItemStack>

    override fun getIconItemStack(): ItemStack {
        return ItemStack(ModItems.frame)
    }

    override fun getTabIconItem(): Item? {
        return this.iconItemStack.item
    }

    override fun displayAllRelevantItems(list: MutableList<ItemStack>) {
        this.list = list
        for (item in items)
            addItem(item)
        addEnchantmentBooksToList(list, *(relevantEnchantmentTypes ?: arrayOf()))
    }

    private fun addItem(item: Item) {
        val tempList = mutableListOf<ItemStack>()
        item.getSubItems(item, this, tempList)
        if (item == tabIconItem)
            this.list.addAll(0, tempList)
        else
            this.list.addAll(tempList)
    }

    private val items = ArrayList<Item>()

    fun set(block: Block) {
        val item = Item.getItemFromBlock(block) ?: return
        items.add(item)
        block.setCreativeTab(this)
    }

    fun set(item: Item) {
        items.add(item)
        item.creativeTab = this
    }
}

