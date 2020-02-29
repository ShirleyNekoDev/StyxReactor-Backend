package de.groovybyte.gamejam.styxreactor.chat

import de.groovybyte.gamejam.styxreactor.datatransfer.GameSession
import de.groovybyte.gamejam.styxreactor.datatransfer.client2server.ClientMessage
import de.groovybyte.gamejam.styxreactor.datatransfer.server2client.ServerMessage
import de.groovybyte.gamejam.styxreactor.utils.WebSocketController

object ChatController {
    fun receive(ctx: WebSocketController<GameSession>.ClientContext, msg: ClientMessage) {
        ctx.broadcast(ServerMessage("chatmessage", msg.payload<ChatMessage>()))
    }
}
