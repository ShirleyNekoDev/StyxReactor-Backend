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
            val packet = ClientMessage.parse(ctx, objectMapper, jsonMessage.value())
            when (packet.command) {
                "test_start" -> ctx.session.startTestMessageSender()
                "test_stop" -> ctx.session.stopTestMessageSender()
                "echo" -> ctx.send(ServerMessage("echo", Echo(jsonMessage.value())))
                "chatmessage" -> ChatController.receive(ctx, packet)
            }
        } catch (ex: InvalidPayloadException) {
            ctx.sendError("invalid payload", ex.payload)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ctx.sendError("unknown error", "")
        }
    }

    override fun onClose(
        ctx: ClientContext,
        closeStatus: WebSocketCloseStatus
    ) {
        // clean up player
    }
}
