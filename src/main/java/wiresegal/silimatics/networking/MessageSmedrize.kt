package wiresegal.silimatics.networking

import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import wiresegal.silimatics.common.item.ItemLens
import wiresegal.silimatics.common.lens.LensOculator
import java.util.*

class MessageSmedrize(var player: UUID? = null, var smedry: Boolean = false) : MessageBase<MessageSmedrize>() {

    override fun onMessage(message: MessageSmedrize, ctx: MessageContext): IMessage? {
        if (ctx.side.isClient) {
            val world = Minecraft.getMinecraft().theWorld
            try {
                for (player in world.playerEntities) {
                    if (player.uniqueID == message.player) {
                        var persist = player.entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG)
                        if (persist == null)
                            persist = NBTTagCompound()

                        persist.setBoolean(ItemLens.OCULATOR_TAG, message.smedry)
                        player.entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persist)
                        break
                    }
                }
            } catch(e: Exception) {
                e.printStackTrace() //there's this weird NPE that happens for the first time you access a world per time you launch MC
                //http://hastebin.com/oqekakavov.avrasm
            }
        }
        return null
    }

    override fun fromBytes(buf: ByteBuf) {
        smedry = buf.readBoolean()
        player = UUID(buf.readLong(), buf.readLong())
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeBoolean(smedry)
        buf.writeLong(player?.mostSignificantBits ?: 0)
        buf.writeLong(player?.leastSignificantBits ?: 0)
    }
}
