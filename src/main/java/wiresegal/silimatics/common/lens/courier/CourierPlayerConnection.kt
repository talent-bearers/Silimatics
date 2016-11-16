package wiresegal.silimatics.common.lens.courier

import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.FMLLog
import wiresegal.silimatics.api.lens.ILens
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.lens.LensCourier
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.*

class CourierPlayerConnection(var socket: Socket) : Thread() {

    var username: String? = null

    var open = true

    lateinit var input: DataInputStream
    lateinit var output: DataOutputStream

    var server = FMLCommonHandler.instance().minecraftServerInstance

    override fun run() {
        try {
            input = DataInputStream(BufferedInputStream(socket.inputStream))
            output = DataOutputStream(BufferedOutputStream(socket.outputStream))

            synchronized (LensCourier.manager) {
                var retryCount = 0

                while (username == null && retryCount <= 100) {
                    try {
                        val l = Collections.synchronizedList((server.playerList.playerList as ArrayList<*>).clone() as List<*>)

                        for (obj in l) {
                            if (obj is EntityPlayerMP) {
                                val playerIP = obj.playerIP

                                if (!server.isDedicatedServer && playerIP == "local" && !LensCourier.manager.foundLocal) {
                                    LensCourier.manager.foundLocal = true
                                    username = obj.name
                                    break
                                } else if (playerIP == socket.inetAddress.hostAddress) {
                                    username = obj.name
                                    break
                                }
                            }
                        }

                        retryCount++
                        Thread.sleep(50)
                    } catch (ignored: Exception) {
                    }

                }

                if (username == null) {
                    FMLLog.warning("VoiceServer: Unable to trace connection's IP address.")
                    kill()
                    return
                } else {
                    FMLLog.info("VoiceServer: Traced IP in $retryCount attempts.")
                }
            }
        } catch (e: Exception) {
            FMLLog.warning("VoiceServer: Error while starting server-based connection.")
            e.printStackTrace()
            open = false
        }

        //Main client listen thread
        Thread {
            while (open) {
                try {
                    val byteCount = this@CourierPlayerConnection.input.readShort()
                    val audioData = ByteArray(byteCount.toInt())
                    this@CourierPlayerConnection.input.readFully(audioData)

                    if (byteCount > 0) {
                        LensCourier.manager.sendToPlayers(byteCount, audioData, this@CourierPlayerConnection)
                    }
                } catch (e: Exception) {
                    open = false
                }

            }

            if (!open) {
                kill()
            }
        }.start()
    }

    fun kill() {
        try {
            input.close()
            output.close()
            socket.close()

            LensCourier.manager.connections.remove(this)
        } catch (e: Exception) {
            FMLLog.warning("VoiceServer: Error while stopping server-based connection.")
            e.printStackTrace()
        }

    }

    fun sendToPlayer(byteCount: Short, audioData: ByteArray) {
        if (!open) {
            kill()
        }

        try {
            output.writeShort(byteCount.toInt())
            output.write(audioData)

            output.flush()
        } catch (e: Exception) {
            FMLLog.warning("VoiceServer: Error while sending data to player.")
            e.printStackTrace()
        }

    }

    fun canListen(): Boolean {
        for (itemStack in player?.inventory?.mainInventory!!) {
            if (canListen(itemStack)) {
                return true
            }
        }

        for (itemStack in player?.inventory?.offHandInventory!!) {
            if (canListen(itemStack)) {
                return true
            }
        }

        return false
    }

    fun canListen(itemStack: ItemStack?): Boolean {
        if (itemStack != null) {
            println("hi")
            if (itemStack.item is ILens && itemStack.metadata == EnumSandType.VIEW.ordinal) {
                return true
            }
        }

        return false
    }

    val currentChannel: Int
        get() = 69

    val player: EntityPlayerMP?
        get() = server.playerList.getPlayerByUsername(username)
}
