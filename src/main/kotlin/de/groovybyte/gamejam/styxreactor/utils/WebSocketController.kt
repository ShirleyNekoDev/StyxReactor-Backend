package de.groovybyte.gamejam.styxreactor.utils

import de.groovybyte.gamejam.styxreactor.datatransfer.Message
import io.jooby.*
import org.slf4j.Logger
import java.io.IOException
import java.util.*

abstract class WebSocketController<P>(
    protected val log: Logger
) : WebSocket.Initializer, AutoCloseable {
    private val clients: MutableSet<ClientContext> = HashSet()
    protected val connectedClients: Map<UUID, ClientContext>
        get() = clients.map { it.id to it }.toMap()

    private fun checkConnections() = clients.forEach { it.checkConnection() }

    protected abstract fun initialPayload(ctx: Context): P

    override fun init(ctx: Context, configurer: WebSocketConfigurer) {
        val clientId = UUID.randomUUID()
        val userAgent = ctx.header("User-Agent").value()
        log.info("[{}] Agent={}", clientId, userAgent)

        lateinit var clientCtx: ClientContext

        configurer.apply {
            onConnect { ws ->
                checkConnections()
                clientCtx = ClientContext(
                    webSocket = ws,
                    id = clientId,
                    userAgent = userAgent,
                    payload = initialPayload(ctx)
                )
                clients.add(clientCtx)
                log.info("[{}] New client connected", clientCtx)
                onConnect(clientCtx)
            }
            onMessage { _, message ->
                checkConnections()
                log.info("[{}] >> {}", clientCtx, message.value())
                onMessage(clientCtx, message)
            }
            onClose { _, closeStatus ->
                log.info(
                    "[{}] Closed - code={} reason={}",
                    clientCtx, closeStatus.code, closeStatus.reason
                )
                clients.remove(clientCtx)
                onClose(clientCtx, closeStatus)
            }
            onError { ws, cause ->
                if (cause is IOException && cause.message!! == "An existing connection was forcibly closed by the remote host") {
                    log.info("[{}] Socket closed", clientCtx)
                    ws.close(WebSocketCloseStatus.HARSH_DISCONNECT)
                } else {
                    log.warn("[$clientCtx] Error", cause)
                    ws.close(WebSocketCloseStatus.SERVER_ERROR)
                }
            }
        }
    }

    inner class ClientContext(
        private val webSocket: WebSocket,
        val id: UUID,
        val userAgent: String,
        val payload: P
    ) {
        val others: Collection<ClientContext> get() = clients - this

        fun broadcast(message: Message<*>, includeSelf: Boolean = true) {
            synchronized(clients) {
                val receivers = if (includeSelf) clients else others
                receivers.forEach { it.send(message) }
            }
        }

        fun send(message: Message<*>) {
            webSocket.render(message)
        }

        // INTERNAL ----------------------

        fun checkConnection() {
            if (!webSocket.isOpen) {
                log.info("[{}] Socket closed", id)
                webSocket.close(WebSocketCloseStatus.HARSH_DISCONNECT)
            }
        }

        fun destroy(closeStatus: WebSocketCloseStatus) {
            log.debug("Disconnecting WS client [{}]", id)
            webSocket.close(closeStatus)
        }

        override fun toString(): String = id.toString()
    }

    protected abstract fun onConnect(ctx: ClientContext)

    protected abstract fun onMessage(
        ctx: ClientContext,
        jsonMessage: WebSocketMessage
    )

    protected abstract fun onClose(
        ctx: ClientContext,
        closeStatus: WebSocketCloseStatus
    )

    override fun close() {
        clients.forEach { it.destroy(WebSocketCloseStatus.SERVICE_RESTARTED) }
    }
}
