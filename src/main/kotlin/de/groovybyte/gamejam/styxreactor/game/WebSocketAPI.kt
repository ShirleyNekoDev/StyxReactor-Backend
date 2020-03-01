package de.groovybyte.gamejam.styxreactor.game

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import de.groovybyte.gamejam.styxreactor.framework.ClientContext
import de.groovybyte.gamejam.styxreactor.framework.WebSocketController
import de.groovybyte.gamejam.styxreactor.game.chat.ChatController
import de.groovybyte.gamejam.styxreactor.game.chat.ChatMessage
import de.groovybyte.gamejam.styxreactor.game.datatransfer.client2server.ClientMessage
import de.groovybyte.gamejam.styxreactor.game.datatransfer.exceptions.InvalidPayloadException
import de.groovybyte.gamejam.styxreactor.game.datatransfer.sendError
import de.groovybyte.gamejam.styxreactor.game.datatransfer.server2client.ServerMessage
import de.groovybyte.gamejam.styxreactor.game.datatransfer.utils.asEcho
import de.groovybyte.gamejam.styxreactor.game.ping.PingController
import de.groovybyte.gamejam.styxreactor.game.world.FieldPosition
import de.groovybyte.gamejam.styxreactor.game.world.WorldController
import io.jooby.WebSocketCloseStatus
import io.jooby.WebSocketMessage
import org.slf4j.Logger

class WebSocketAPI(
    log: Logger,
    private val objectMapper: ObjectMapper
) : WebSocketController(log) {

    override fun onConnect(
        ctx: ClientContext
    ) {
//        if(ctx.headers.containsKey("Reconnect")) {
//
//        } else {
//            MatchStore.getSession(ctx).createPlayer()
//        }
        // init player or something
        WorldController.sendWorld(ctx)
    }

    override fun onMessage(
        ctx: ClientContext,
        jsonMessage: WebSocketMessage
    ) {
        try {
            routeMessage(
                ctx,
                ClientMessage.parse(objectMapper, jsonMessage.value())
            )
        } catch (ex: InvalidPayloadException) {
            ctx.sendError("invalid payload", ex.payload)
        } catch (ex: JsonMappingException) {
            ctx.sendError("invalid body", jsonMessage.value())
        } catch (ex: Exception) {
            ex.printStackTrace()
            ctx.sendError("unknown error", "")
        }
    }

    fun routeMessage(ctx: ClientContext, packet: ClientMessage) {
        when (packet.command) {
//            "test_start" -> packet.ctx.session.startTestMessageSender()
//            "test_stop" -> packet.ctx.session.stopTestMessageSender()
            "echo" -> ctx.send(ServerMessage("echo", packet.asEcho()))
            "chatmessage" -> ChatController.receive(ctx, packet)
            "ping" -> PingController.receive(ctx, packet)
        }
    }

    override fun onClose(
        ctx: ClientContext,
        closeStatus: WebSocketCloseStatus
    ) {
        // clean up player
    }
}
