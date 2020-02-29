package de.groovybyte.gamejam.styxreactor.framework

import de.groovybyte.gamejam.styxreactor.game.ClientId
import de.groovybyte.gamejam.styxreactor.game.datatransfer.server2client.ServerMessage

class ClientContext(
    private val wsController: WebSocketController,
    val id: ClientId,
    val headers: Map<String, String>
) {
    val clients = wsController.clients
    val others: Set<ClientId> get() = clients - id

    fun broadcast(message: ServerMessage, includeSelf: Boolean = true) {
        synchronized(clients) {
            val receivers = if (includeSelf) clients else others
            receivers.forEach {
                wsController.getWebSocket(it).render(message)
            }
        }
    }

    fun send(message: ServerMessage) {
        wsController.getWebSocket(id).render(message)
    }

    override fun toString(): String = id.toString()
}
