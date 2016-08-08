package wiresegal.silimatics.common.networking

import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.math.Vec3d
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import wiresegal.silimatics.common.item.ItemLens
import java.util.*

class MessageSyncMotion(var motVec: Vec3d? = null, var fallDist: Float? = null) : MessageBase<MessageSyncMotion>() {

    override fun onMessage(message: MessageSyncMotion, ctx: MessageContext): IMessage? {
        if (ctx.side.isClient) {
            val player = Minecraft.getMinecraft().thePlayer
            val mot = message.motVec ?: return null
            val fall = message.fallDist ?: return null
            player.motionX = mot.xCoord
            player.motionY = mot.yCoord
            player.motionZ = mot.zCoord
            player.fallDistance = fall
        }
        return null
    }

    override fun fromBytes(buf: ByteBuf) {
        motVec = Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble())
        fallDist = buf.readFloat()
    }

    override fun toBytes(buf: ByteBuf) {
        buf.writeDouble(motVec?.xCoord ?: 0.0)
        buf.writeDouble(motVec?.yCoord ?: 0.0)
        buf.writeDouble(motVec?.zCoord ?: 0.0)
        buf.writeFloat(fallDist ?: 0F)
    }
}
