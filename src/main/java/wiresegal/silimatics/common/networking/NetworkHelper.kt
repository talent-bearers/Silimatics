package wiresegal.silimatics.common.networking

import net.minecraft.util.math.BlockPos
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
import net.minecraftforge.fml.relauncher.Side
import wiresegal.silimatics.common.lib.LibMisc

object NetworkHelper : SimpleNetworkWrapper(LibMisc.MODID) {
    private var i = 0

    init {
        registerMessage(MessageSmedrize::class.java, MessageSmedrize::class.java, i++, Side.CLIENT)
        registerMessage(MessageSyncMotion::class.java, MessageSyncMotion::class.java, i++, Side.CLIENT)
    }
}
