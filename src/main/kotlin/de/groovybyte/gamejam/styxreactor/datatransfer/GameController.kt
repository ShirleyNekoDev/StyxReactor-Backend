package de.groovybyte.gamejam.styxreactor.datatransfer

import de.groovybyte.gamejam.styxreactor.utils.WebSocketController
import io.jooby.WebSocketCloseStatus
import io.jooby.WebSocketMessage
import io.jooby.to
import org.slf4j.Logger

class GameController(log: Logger) : WebSocketController<GameSession>(log) {

    override fun initializeData(ctx: ClientContext) = GameSession(ctx)

    override fun onConnect(
        ctx: ClientContext
    ) {
        ctx.data.startTestMessageSender()
//        log.info("[{}] took {} [{}]", ctx.id, ctx.payload.username, ctx.payload.id)
//        ctx.broadcast("[${ctx.payload.username}] connected", false)
//        ctx.send("[${ctx.payload.username}] successfully connected")
//        if (ctx.others.isNotEmpty()) {
//            val otherPlayerNames = ctx.others.joinToString { it.payload.username }
//            ctx.send("[$otherPlayerNames] already connected")
//        }
    }

    private class Echo(val data: String) : Entity<Echo>

    @Suppress("UNCHECKED_CAST")
    override fun onMessage(
        ctx: ClientContext,
        jsonMessage: WebSocketMessage
    ) {
        val msg = jsonMessage.to<Message>()
        when (msg.command) {
            "echo" -> ctx.send(Message("echo", Echo(jsonMessage.value())))
            "chatmessage" -> ctx.broadcast(msg)
        }
    }

    override fun onClose(
        ctx: ClientContext,
        closeStatus: WebSocketCloseStatus
    ) {
//        ctx.broadcast("[${ctx.payload.username}] disconnected (cause=${closeStatus.reason})", false)
    }
}
