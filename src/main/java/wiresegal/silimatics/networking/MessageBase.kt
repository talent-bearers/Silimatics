package wiresegal.silimatics.networking

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

abstract class MessageBase<REQ : IMessage> : IMessage, IMessageHandler<REQ, REQ> {

    override fun onMessage(message: REQ, ctx: MessageContext): REQ? {
        if (ctx.side == Side.SERVER) {
            handleServerSide(message, ctx.serverHandler.playerEntity)
        } else {
            handleClientSide(message, null) //safe because it runs on the client
        }
        return null
    }

    abstract fun handleClientSide(message: REQ, player: EntityPlayer?)

    abstract fun handleServerSide(message: REQ, player: EntityPlayer)
}
