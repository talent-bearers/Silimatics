package wiresegal.silimatics.networking

import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import wiresegal.silimatics.common.lib.LibMisc

object NetworkHelper {
    val INSTANCE = SimpleNetworkWrapper(LibMisc.MODID)
    private var i = 0

    init {
        INSTANCE.registerMessage(MessageSmedrize::class.java, MessageSmedrize::class.java, i++, Side.CLIENT)
    }

    fun tellEveryone(message: IMessage) {
        INSTANCE.sendToAll(message)
    }

    fun tellEveryoneAround(message: IMessage, dim: Int, x: Int, y: Int, z: Int, range: Int) {
        INSTANCE.sendToAllAround(message, NetworkRegistry.TargetPoint(dim, x.toDouble(), y.toDouble(), z.toDouble(), range.toDouble()))
    }

    fun tellEveryoneAround(message: IMessage, dim: Int, pos: BlockPos, range: Int) {
        INSTANCE.sendToAllAround(message, NetworkRegistry.TargetPoint(dim, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), range.toDouble()))
    }

    fun tellEveryoneAround(message: IMessage, point: NetworkRegistry.TargetPoint) {
        INSTANCE.sendToAllAround(message, point)
    }
}
