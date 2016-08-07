package wiresegal.silimatics.common.util

import net.minecraft.item.ItemStack
import net.minecraftforge.items.IItemHandler

/**
 * @author WireSegal
 * Created at 7:09 PM on 8/6/16.
 */
open class SlotWrapperHandler(val inventoryMain: IItemHandler, val slotRange: IntRange, val shift: Int) : IItemHandler {
    override fun getStackInSlot(slot: Int): ItemStack? {
        if (slot < 0 || slot >= slots)
            throw RuntimeException("Slot $slot not in valid range - [0,$slots)")
        return inventoryMain.getStackInSlot(slot + shift)
    }

    override fun insertItem(slot: Int, stack: ItemStack?, simulate: Boolean): ItemStack? {
        if (slot < 0 || slot >= slots)
            throw RuntimeException("Slot $slot not in valid range - [0,$slots)")
        return inventoryMain.insertItem(slot + shift, stack, simulate)
    }

    override fun getSlots(): Int {
        return slotRange.endInclusive - slotRange.start + 1
    }

    override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack? {
        if (slot < 0 || slot >= slots)
            throw RuntimeException("Slot $slot not in valid range - [0,$slots)")
        return inventoryMain.extractItem(slot + shift, amount, simulate)
    }
}
