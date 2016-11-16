package wiresegal.silimatics.common.lens.courier

import net.minecraftforge.fml.common.FMLLog
import java.net.ServerSocket
import java.net.Socket
import java.util.*

class CourierManager {
    var connections: MutableSet<CourierPlayerConnection> = HashSet()

    var serverSocket: ServerSocket = ServerSocket(4512)

    var running: Boolean = false

    var foundLocal = false

    lateinit var listenThread: Thread
    @Volatile lateinit var socket: Socket

    fun start() {
        FMLLog.info("VoiceServer: Starting up server...")

        try {
            running = true
            //serverSocket = ServerSocket(4512)
            println("Voice1")
            /*socket = serverSocket.accept()
            listenThread = ListenThread()
            listenThread.start()*/

            //serverSocket = ServerSocket(8523)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun stop() {
        try {
            FMLLog.info("VoiceServer: Shutting down server...")

            try {
                listenThread.interrupt()
            } catch (ignored: Exception) {
            }

            foundLocal = false

            try {
                serverSocket.close()
            } catch (ignored: Exception) {
            }

        } catch (e: Exception) {
            FMLLog.warning("VoiceServer: Error while shutting down server.")
            e.printStackTrace()
        }

        running = false
    }

    fun sendToPlayers(byteCount: Short, audioData: ByteArray, connection: CourierPlayerConnection) {
        if (connection.player == null) {
            return
        }

        val channel = connection.currentChannel

        if (channel == 0) {
            return
        }

        for (iterConn in connections) {
            if (iterConn.player == null || iterConn == connection || !iterConn.canListen()) {
                continue
            }
            iterConn.sendToPlayer(byteCount, audioData)
        }
    }

    inner class ListenThread : Thread() {
        init {
            isDaemon = true
            name = "VoiceServer Listen Thread"
        }

        override fun run() {
            while (running) {
                try {
                    println("Voice2")
                    val connection = CourierPlayerConnection(socket)
                    println("Voice3")
                    connection.start()
                    println("Voice4")
                    connections.add(connection)
                    println("Voice5")
                    FMLLog.info("VoiceServer: Accepted new connection.")
                } catch (e: Exception) {
                    FMLLog.warning("VoiceServer: Error while accepting connection.")
                    e.printStackTrace()
                } finally {
                    FMLLog.warning("Finally")
                }

            }
            FMLLog.warning("Out of the loop")
        }
    }
}
