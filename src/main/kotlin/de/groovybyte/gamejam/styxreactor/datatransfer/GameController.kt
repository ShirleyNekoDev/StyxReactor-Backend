package de.groovybyte.gamejam.styxreactor.datatransfer

import com.fasterxml.jackson.databind.ObjectMapper
import de.groovybyte.gamejam.styxreactor.chat.ChatController
import de.groovybyte.gamejam.styxreactor.datatransfer.client2server.ClientMessage
import de.groovybyte.gamejam.styxreactor.datatransfer.exceptions.InvalidPayloadException
import de.groovybyte.gamejam.styxreactor.datatransfer.server2client.ServerMessage
import de.groovybyte.gamejam.styxreactor.utils.WebSocketController
import io.jooby.WebSocketCloseStatus
import io.jooby.WebSocketMessage
import org.slf4j.Logger

class GameController(
    log: Logger,
    val objectMapper: ObjectMapper
) : WebSocketController<GameSession>(log) {

    override fun initializeData(ctx: ClientContext) = GameSession(ctx)

    override fun onConnect(
        ctx: ClientContext
    ) {
        // init player or something
    }

    private class Echo(val data: String) : Entity<Echo>

    private fun ClientMessage.asEcho() = Echo(packetData)

    private fun ClientContext.sendError(type: String, data: Any? = null) =
        send(
            ServerMessage(
                command = "error",
                payload = Error(type = type, data = data)
            )
        )

    @Suppress("UNCHECKED_CAST")
    override fun onMessage(
        ctx: ClientContext,
        jsonMessage: WebSocketMessage
    ) {
        try {
            routeMessage(
                ClientMessage.parse(ctx, objectMapper, jsonMessage.value())
            )
        } catch (ex: InvalidPayloadException) {
            ctx.sendError("invalid payload", ex.payload)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ctx.sendError("unknown error", "")
        }
    }

    fun routeMessage(packet: ClientMessage) {
        when (packet.command) {
            "test_start" -> packet.ctx.session.startTestMessageSender()
            "test_stop" -> packet.ctx.session.stopTestMessageSender()
            "echo" -> packet.ctx.send(ServerMessage("echo", packet.asEcho()))
            "chatmessage" -> ChatController.receive(packet.ctx, packet)
        }
    }

    override fun onClose(
        ctx: ClientContext,
        closeStatus: WebSocketCloseStatus
    ) {
        // clean up player
    }
}
