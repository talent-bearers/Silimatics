package wiresegal.silimatics.common.item

import net.minecraft.client.renderer.color.IItemColor
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextComponentString
import net.minecraft.util.text.TextFormatting
import net.minecraft.world.World
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.ServerChatEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import wiresegal.silimatics.common.core.ItemNBTHelper
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.lib.LibNames
import wiresegal.silimatics.common.util.color
import wiresegal.zenmodelloader.common.core.IItemColorProvider
import wiresegal.zenmodelloader.common.items.base.ItemMod
import java.util.*

/**
 * Created by Elad on 10/22/2016.
 */
class ItemCommunicatorShard : ItemMod(LibNames.COMMUNICATOR_SHARD), IItemColorProvider {
    override fun getItemColor(): IItemColor? {
        return IItemColor { stack, i -> getColor(stack) }
    }

    fun getColor(stack: ItemStack) = EnumSandType.VIEW.glassColor + ItemNBTHelper.getUUID(stack, "uuid", false)!!.hashCode() / 2

    fun getClosestColor(color: Int): TextFormatting {
        var distance = Math.abs(TextFormatting.values()[0].color()!! - color)
        var idx = 0
        for(c in TextFormatting.values().indices.filter { it != 0 && TextFormatting.values()[it].color() != null}) {
            val cDistance = Math.abs(TextFormatting.values()[c].color()!! - color)
            if(cDistance < distance) {
                idx = c
                distance = cDistance
            }
        }
        return TextFormatting.values()[idx]
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
                                player.addChatComponentMessage(TextComponentString(getClosestColor(getColor(player.heldItemMainhand!!)).toString() + "Communicator's Shard: ").appendSibling(ev.component))
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