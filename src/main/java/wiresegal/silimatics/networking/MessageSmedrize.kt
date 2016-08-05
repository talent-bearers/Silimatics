package wiresegal.silimatics.networking

import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import wiresegal.silimatics.common.item.ItemLens

class MessageSmedrize(internal var smedry: Boolean) : MessageBase<MessageSmedrize>() {
    constructor() : this(false)

    override fun handleClientSide(message: MessageSmedrize, player: EntityPlayer?) {
        if(Minecraft.getMinecraft() != null && Minecraft.getMinecraft().thePlayer != null)
            Minecraft.getMinecraft().thePlayer.entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG).setBoolean(ItemLens.OCULATOR_TAG, message.smedry)
    }


    override fun handleServerSide(message: MessageSmedrize, player: EntityPlayer) {
        //NO-OP
    }

    override fun fromBytes(buf: ByteBuf) {
        smedry = buf.readBoolean()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeBoolean(smedry)
    }
}
