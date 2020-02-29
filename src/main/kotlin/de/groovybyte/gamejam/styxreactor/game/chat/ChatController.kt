package de.groovybyte.gamejam.styxreactor.game.chat

import de.groovybyte.gamejam.styxreactor.framework.ClientContext
import de.groovybyte.gamejam.styxreactor.game.datatransfer.client2server.ClientMessage
import de.groovybyte.gamejam.styxreactor.game.datatransfer.server2client.ServerMessage

object ChatController {
    fun receive(ctx: ClientContext, msg: ClientMessage) {
        ctx.broadcast(ServerMessage("chatmessage", msg.payload<ChatMessage>()))
    }
}
