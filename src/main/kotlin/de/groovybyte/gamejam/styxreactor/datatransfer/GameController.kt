package de.groovybyte.gamejam.styxreactor.datatransfer

import de.groovybyte.gamejam.styxreactor.chat.ChatController
import de.groovybyte.gamejam.styxreactor.chat.ChatMessage
import de.groovybyte.gamejam.styxreactor.player.PlayerId
import de.groovybyte.gamejam.styxreactor.utils.WebSocketController
import io.jooby.Context
import io.jooby.WebSocketCloseStatus
import io.jooby.WebSocketMessage
import io.jooby.to
import org.slf4j.Logger
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class GameController(log: Logger) : WebSocketController<GameSession>(log) {
    override fun initialPayload(ctx: Context): GameSession =
        GameSession()

    override fun onConnect(
        ctx: ClientContext
    ) {
//        log.info("[{}] took {} [{}]", ctx.id, ctx.payload.username, ctx.payload.id)
//        ctx.broadcast("[${ctx.payload.username}] connected", false)
//        ctx.send("[${ctx.payload.username}] successfully connected")
//        if (ctx.others.isNotEmpty()) {
//            val otherPlayerNames = ctx.others.joinToString { it.payload.username }
//            ctx.send("[$otherPlayerNames] already connected")
//        }
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate({
            ctx.send(
                Message(
                    "chatmessage", ChatMessage(
                        author = PlayerId.SYSTEM_ID,
                        message = "hello world"
                    )
                )
            )
        }, 1, 1, TimeUnit.SECONDS)
    }

    class Echo(val data: String) : Entity<Echo>

    @Suppress("UNCHECKED_CAST")
    override fun onMessage(
        ctx: ClientContext,
        jsonMessage: WebSocketMessage
    ) {
        val msg = jsonMessage.to<Message<*>>()
        when (msg.command) {
            "echo" -> {
                ctx.send(Message("echo", Echo(jsonMessage.value())))
            }
            "chatmessage" -> ChatController.receiveMessage(ctx, msg as Message<ChatMessage>)
        }
//        ctx.broadcast("[${ctx.payload.username}] >> ${message.value()}")
    }

    override fun onClose(
        ctx: ClientContext,
        closeStatus: WebSocketCloseStatus
    ) {
//        ctx.broadcast("[${ctx.payload.username}] disconnected (cause=${closeStatus.reason})", false)
    }
}
