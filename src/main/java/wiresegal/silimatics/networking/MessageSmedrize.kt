package wiresegal.silimatics.networking

import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import wiresegal.silimatics.common.item.ItemLens

class MessageSmedrize(var smedry: Boolean = false) : MessageBase<MessageSmedrize>() {

    override fun onMessage(message: MessageSmedrize, ctx: MessageContext): IMessage? {
        val player = Minecraft.getMinecraft().thePlayer
        var persist = player.entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG)
        if (persist == null) {
            persist = NBTTagCompound()
            player.entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persist)
        }
        persist.setBoolean(ItemLens.OCULATOR_TAG, message.smedry)
        return null
    }

    override fun fromBytes(buf: ByteBuf) {
        smedry = buf.readBoolean()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeBoolean(smedry)
    }
}
