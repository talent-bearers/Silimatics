package wiresegal.silimatics.common.lens.courier

import java.net.ServerSocket
import java.net.SocketException
import java.util.*

class CourierManager {
    var connections: MutableSet<CourierConnection> = HashSet()

    var serverSocket: ServerSocket? = null

    var running: Boolean = false

    var foundLocal = false

    var listenThread: ListenThread? = null

    fun start() {
        println("Starting up voice server...")

        try {
            running = true
            serverSocket = ServerSocket(VOICE_PORT)
            listenThread = ListenThread()
            (listenThread as Thread).start()
        } catch (e: Exception) {
        }

    }

    fun stop() {
        try {
            println("Shutting down voice server...")

            try {
                listenThread?.interrupt()
            } catch (e: Exception) {
            }

            foundLocal = false

            try {
                serverSocket!!.close()
                serverSocket = null
            } catch (e: Exception) {
            }

        } catch (e: Exception) {
            System.err.println("Error while shutting down voice server.")
            e.printStackTrace()
        }

        running = false
    }

    fun sendToPlayers(byteCount: Short, audioData: ByteArray, connection: CourierConnection) {
        if (connection.player == null) {
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
                println("running")
                try {
                    val s = serverSocket!!.accept()
                    val connection = CourierConnection(s)
                    connection.start()
                    connections.add(connection)

                    println("VoiceServer: Accepted new connection.")
                } catch (ignored: SocketException) {
                } catch (ignored: NullPointerException) {
                } catch (e: Exception) {
                    System.err.println("VoiceServer: Error while accepting connection.")
                    e.printStackTrace()
                }

            }
            println("n'running")
        }
    }

    companion object {
        val VOICE_PORT = 21452
    }
}

