package de.groovybyte.gamejam.styxreactor.framework

import de.groovybyte.gamejam.styxreactor.game.ClientId
import io.jooby.*
import org.slf4j.Logger
import java.io.IOException
import java.util.*
import kotlin.collections.HashMap

abstract class WebSocketController(
    protected val log: Logger
) : WebSocket.Initializer, AutoCloseable {
    private val connectedWebsockets: MutableMap<ClientId, WebSocket> = HashMap()
    private val connectedClients: MutableMap<ClientId, ClientContext> = HashMap()
    val clients: Set<ClientId> get() = connectedClients.keys

    fun getWebSocket(id: ClientId): WebSocket = connectedWebsockets[id]!!
    fun getClient(id: ClientId): ClientContext = connectedClients[id]!!

    private fun checkConnections() = connectedWebsockets.forEach { (id, ws) ->
        if (!ws.isOpen) {
            log.info("[{}] Socket closed", id)
            ws.close(WebSocketCloseStatus.HARSH_DISCONNECT)
        }
    }

    private fun connectClient(ctx: ClientContext, ws: WebSocket) {
        connectedClients[ctx.id] = ctx
        connectedWebsockets[ctx.id] = ws
    }

    private fun disconnectClient(clientId: ClientId) {
        connectedClients.remove(clientId)
        connectedWebsockets.remove(clientId)
    }

    override fun init(ctx: Context, configurer: WebSocketConfigurer) {
        val clientId = UUID.randomUUID()
        log.info("[{}]", clientId)

        lateinit var clientCtx: ClientContext

        configurer.apply {
            onConnect { ws ->
                checkConnections()
                clientCtx = ClientContext(
                    wsController = this@WebSocketController,
                    id = clientId,
                    headers = ctx.headerMap()
                )

                connectClient(clientCtx, ws)
                log.info("[{}] New client connected", clientCtx)
                onConnect(clientCtx)
            }
            onMessage { _, message ->
                checkConnections()
                log.info("[{}] >> {}", clientCtx, message.value())
                onMessage(clientCtx, message)
            }
            onClose { _, closeStatus ->
                disconnectClient(clientCtx.id)
                log.info(
                    "[{}] Closed - code={} reason={}",
                    clientCtx, closeStatus.code, closeStatus.reason
                )
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
        connectedWebsockets.forEach { (id, ws) ->
            log.debug("Disconnecting WS client [{}]", id)
            ws.close(WebSocketCloseStatus.SERVICE_RESTARTED)
        }
    }
}
