package wiresegal.silimatics.common.core

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTBase
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import java.util.*

object ItemNBTHelper {

    private val EMPTY_INT_ARRAY = IntArray(0)
    private val EMPTY_UUID = UUID(0, 0)

    fun detectNBT(stack: ItemStack) = stack.hasTagCompound()
    fun getNBT(stack: ItemStack) = initNBT(stack).tagCompound!!
    fun initNBT(stack: ItemStack): ItemStack {
        if (!detectNBT(stack))
            stack.tagCompound = NBTTagCompound()
        return stack
    }

    fun removeEntry(stack: ItemStack, tag: String) = getNBT(stack).removeTag(tag)
    fun removeUUID(stack: ItemStack, tag: String) {
        getNBT(stack).removeTag(tag + "Most")
        getNBT(stack).removeTag(tag + "Least")
    }
    fun verifyExistence(stack: ItemStack, tag: String) = getNBT(stack).hasKey(tag)
    fun verifyUUIDExistence(stack: ItemStack, tag: String) = verifyExistence(stack, tag + "Most") && verifyExistence(stack, tag + "Least")

    fun setBoolean(stack: ItemStack, tag: String, b: Boolean) = getNBT(stack).setBoolean(tag, b)
    fun setByte(stack: ItemStack, tag: String, b: Byte) = getNBT(stack).setByte(tag, b)
    fun setShort(stack: ItemStack, tag: String, s: Short) = getNBT(stack).setShort(tag, s)
    fun setInt(stack: ItemStack, tag: String, i: Int) = getNBT(stack).setInteger(tag, i)
    fun setIntArray(stack: ItemStack, tag: String, arr: IntArray) = getNBT(stack).setIntArray(tag, arr)
    fun setLong(stack: ItemStack, tag: String, l: Long) = getNBT(stack).setLong(tag, l)
    fun setFloat(stack: ItemStack, tag: String, f: Float) = getNBT(stack).setFloat(tag, f)
    fun setDouble(stack: ItemStack, tag: String, d: Double) = getNBT(stack).setDouble(tag, d)
    fun setCompound(stack: ItemStack, tag: String, cmp: NBTTagCompound) = getNBT(stack).setTag(tag, cmp)
    fun setString(stack: ItemStack, tag: String, s: String) = getNBT(stack).setString(tag, s)
    fun setList(stack: ItemStack, tag: String, list: NBTTagList) = getNBT(stack).setTag(tag, list)
    fun setUUID(stack: ItemStack, tag: String, uuid: UUID) = getNBT(stack).setUniqueId(tag, uuid)


    fun getBoolean(stack: ItemStack, tag: String, defaultExpected: Boolean) =
            if (verifyExistence(stack, tag)) getNBT(stack).getBoolean(tag) else defaultExpected

    fun getByte(stack: ItemStack, tag: String, defaultExpected: Byte) =
            if (verifyExistence(stack, tag)) getNBT(stack).getByte(tag) else defaultExpected

    fun getShort(stack: ItemStack, tag: String, defaultExpected: Short) =
            if (verifyExistence(stack, tag)) getNBT(stack).getShort(tag) else defaultExpected

    fun getInt(stack: ItemStack, tag: String, defaultExpected: Int) =
            if (verifyExistence(stack, tag)) getNBT(stack).getInteger(tag) else defaultExpected

    fun getIntArray(stack: ItemStack, tag: String) =
            if (verifyExistence(stack, tag)) getNBT(stack).getIntArray(tag) else EMPTY_INT_ARRAY

    fun getLong(stack: ItemStack, tag: String, defaultExpected: Long) =
            if (verifyExistence(stack, tag)) getNBT(stack).getLong(tag) else defaultExpected

    fun getFloat(stack: ItemStack, tag: String, defaultExpected: Float) =
            if (verifyExistence(stack, tag)) getNBT(stack).getFloat(tag) else defaultExpected

    fun getDouble(stack: ItemStack, tag: String, defaultExpected: Double) =
            if (verifyExistence(stack, tag)) getNBT(stack).getDouble(tag) else defaultExpected

    fun getCompound(stack: ItemStack, tag: String, nullifyOnFail: Boolean) =
            if (verifyExistence(stack, tag)) getNBT(stack).getCompoundTag(tag) else if (nullifyOnFail) null else NBTTagCompound()

    fun getString(stack: ItemStack, tag: String, defaultExpected: String?) =
            if (verifyExistence(stack, tag)) getNBT(stack).getString(tag) else defaultExpected

    fun getList(stack: ItemStack, tag: String, nbtClass: Class<NBTBase>, nullifyOnFail: Boolean) =
        getList(stack, tag, nbtClass.newInstance().id.toInt(), nullifyOnFail)

    fun getList(stack: ItemStack, tag: String, objType: Int, nullifyOnFail: Boolean) =
            if (verifyExistence(stack, tag)) getNBT(stack).getTagList(tag, objType) else if (nullifyOnFail) null else NBTTagList()

    fun getUUID(stack: ItemStack, tag: String, nullifyOnFail: Boolean) =
            if (verifyUUIDExistence(stack, tag)) getNBT(stack).getUniqueId(tag) else if (nullifyOnFail) null else EMPTY_UUID

}
