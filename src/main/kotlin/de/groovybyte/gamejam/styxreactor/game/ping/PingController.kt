package de.groovybyte.gamejam.styxreactor.game.ping

import de.groovybyte.gamejam.styxreactor.framework.ClientContext
import de.groovybyte.gamejam.styxreactor.game.datatransfer.client2server.ClientMessage
import de.groovybyte.gamejam.styxreactor.game.datatransfer.server2client.ServerMessage

object PingController {
    fun receive(ctx: ClientContext, msg: ClientMessage) {
        println(msg.payload<PingMessage>())
        ctx.broadcast(ServerMessage("ping", msg.payload<PingMessage>()))
    }
}
