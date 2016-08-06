package wiresegal.silimatics.common.lens.courier

import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.FMLCommonHandler
import wiresegal.silimatics.common.core.ModItems
import wiresegal.silimatics.common.item.EnumSandType
import wiresegal.silimatics.common.item.ItemLensFrames.Companion.getLensStack
import wiresegal.silimatics.common.lens.LensCourier
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket
import java.util.*

class CourierConnection(var socket: Socket) : Thread() {

    var username: String? = null

    var open = true

    var input: DataInputStream? = null
    var output: DataOutputStream? = null

    var server = FMLCommonHandler.instance().minecraftServerInstance

    override fun run() {
        try {
            input = DataInputStream(BufferedInputStream(socket.inputStream))
            output = DataOutputStream(BufferedOutputStream(socket.outputStream))

            synchronized(LensCourier.manager) {
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
                    } catch (e: Exception) {
                    }

                }

                if (username == null) {
                    System.err?.println("Unable to trace voice connection's IP address.")
                    kill()
                    return
                } else
                    println("Traced IP for courier lens server in $retryCount attempts.")

            }
        } catch (e: Exception) {
            System.err?.println("Error while starting server voice connection.")
            e.printStackTrace()
            open = false
        }

        Thread {
            while (open) {
                try {
                    val byteCount = this@CourierConnection.input?.readShort() ?: throw NullPointerException()
                    val audioData = ByteArray(byteCount.toInt())
                    this@CourierConnection.input?.readFully(audioData)

                    if (byteCount > 0)
                        LensCourier.manager.sendToPlayers(byteCount, audioData, this@CourierConnection)

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
            input?.close()
            output?.close()
            socket.close()

            LensCourier.manager.connections.remove(this)
        } catch (e: Exception) {
            System.err?.println("Error while stopping server voice connection.")
            e.printStackTrace()
        }

    }

    fun sendToPlayer(byteCount: Short, audioData: ByteArray) {
        if (!open)
            kill()
        try {
            output?.writeShort(byteCount.toInt())
            output?.write(audioData)
            output?.flush()
        } catch (e: Exception) {
            System.err?.println("Error while sending voice data to player.")
            e.printStackTrace()
        }

    }

    fun canListen(): Boolean = canListen(player?.getItemStackFromSlot(EntityEquipmentSlot.HEAD))

    fun canListen(itemStack: ItemStack?): Boolean {
        if (itemStack != null) {
            val stack = itemStack.getLensStack()
            return stack.item == ModItems.lens && stack.metadata == EnumSandType.VIEW.ordinal
        }
        return false
    }


    val player: EntityPlayerMP?
        get() = server.playerList.getPlayerByUsername(username)
}
