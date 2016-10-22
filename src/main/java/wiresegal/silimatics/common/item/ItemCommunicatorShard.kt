package wiresegal.silimatics.common.item

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextComponentString
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.lib.LibNames
import wiresegal.zenmodelloader.common.core.IItemColorProvider
import wiresegal.zenmodelloader.common.items.base.ItemMod
import java.util.*

/**
 * Created by Elad on 10/22/2016.
 */
class ItemCommunicatorShard : ItemMod(LibNames.COMMUNICATOR_SHARD), IItemColorProvider {
    override fun getItemColor(): IItemColor? {
        return IItemColor {stack, i -> EnumSandType.VIEW.glassColor}
    }

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }
    @SubscribeEvent
    fun onPlayerChat(ev: ServerChatEvent) {
        if(ev.player.heldItemMainhand?.item != this) return
        for(player in ev.player.worldObj.playerEntities)
            if(player.heldItemMainhand != null && player.heldItemMainhand?.item == this &&
                    ItemNBTHelper.getUUID(player.heldItemMainhand!!, "uuid", true) ==
                            ItemNBTHelper.getUUID(ev.player.heldItemMainhand!!, "uuid", true))
                                player.addChatComponentMessage(TextComponentString("Communicator's Shard: ").appendSibling(ev.component))
        ev.isCanceled = true
    }
    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer?, tooltip: MutableList<String>, advanced: Boolean) {
        if(advanced && ItemNBTHelper.getUUID(stack, "uuid", true) != null) tooltip.add("UUID: " + ItemNBTHelper.getUUID(stack, "uuid", true))
    }
    fun getCommunicatorShardStack(): ItemStack {
        val stack = ItemStack(ModItems.shard, 4)
        ItemNBTHelper.setUUID(stack, "uuid", UUID.randomUUID())
        return stack
    }

    override fun onUpdate(stack: ItemStack, worldIn: World?, entityIn: Entity?, itemSlot: Int, isSelected: Boolean) {
        if(ItemNBTHelper.getUUID(stack, "uuid", true) == null) ItemNBTHelper.setUUID(stack, "uuid", UUID.randomUUID())
    }


}