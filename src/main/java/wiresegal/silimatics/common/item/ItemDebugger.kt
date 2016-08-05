package wiresegal.silimatics.common.item

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import wiresegal.silimatics.common.lib.LibMisc
import wiresegal.zenmodelloader.common.items.base.ItemMod

class ItemDebugger(name: String) : ItemMod(name) {
    val OCULATOR = LibMisc.MODID + ":oculator"
    override fun onLeftClickEntity(stack: ItemStack, player: EntityPlayer, entity: Entity): Boolean {
        entity.entityData?.setTag(EntityPlayer.PERSISTED_NBT_TAG, NBTTagCompound())
        player.entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean(OCULATOR, true);
        return false
    }
}
