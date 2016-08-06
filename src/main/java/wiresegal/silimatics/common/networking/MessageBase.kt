package wiresegal.silimatics.common.networking

import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext

abstract class MessageBase<REQ : IMessage> : IMessage, IMessageHandler<REQ, IMessage> {
    override abstract fun onMessage(message: REQ, ctx: MessageContext): IMessage?
}
