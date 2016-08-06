package wiresegal.silimatics.networking

import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext
import net.minecraftforge.fml.relauncher.Side

abstract class MessageBase<REQ : IMessage> : IMessage, IMessageHandler<REQ, IMessage> {
    override abstract fun onMessage(message: REQ, ctx: MessageContext): IMessage?
}
