package wiresegal.silimatics.networking

import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import wiresegal.silimatics.common.lib.LibMisc

object NetworkHelper {
    var instance: SimpleNetworkWrapper? = null

    init {
        instance = SimpleNetworkWrapper(LibMisc.MODID)
        try {
            instance?.registerMessage(MessageSmedrize::class.java, MessageSmedrize::class.java, 0, Side.CLIENT)
        } catch (server: NoClassDefFoundError) {
            server.printStackTrace()
        }
    }

    //lol
    fun tellEveryone(message: IMessage) {
        instance?.sendToAll(message)
    }

    fun tellEveryoneAround(message: IMessage, dim: Int, x: Int, y: Int, z: Int, range: Int) {
        instance?.sendToAllAround(message, NetworkRegistry.TargetPoint(dim, x.toDouble(), y.toDouble(), z.toDouble(), range.toDouble()))
    }

    fun tellEveryoneAround(message: IMessage, dim: Int, pos: BlockPos, range: Int) {
        instance?.sendToAllAround(message, NetworkRegistry.TargetPoint(dim, pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble(), range.toDouble()))
    }

    fun tellEveryoneAround(message: IMessage, point: NetworkRegistry.TargetPoint) {
        instance?.sendToAllAround(message, point)
    }
}
