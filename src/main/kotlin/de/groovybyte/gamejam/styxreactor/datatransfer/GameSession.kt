package de.groovybyte.gamejam.styxreactor.datatransfer

import de.groovybyte.gamejam.styxreactor.chat.ChatMessage
import de.groovybyte.gamejam.styxreactor.datatransfer.server2client.ServerMessage
import de.groovybyte.gamejam.styxreactor.player.PlayerId
import de.groovybyte.gamejam.styxreactor.utils.WebSocketController
import java.io.Closeable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class GameSession(val ctx: WebSocketController<GameSession>.ClientContext) : Closeable {
    companion object {
        val ex: ScheduledExecutorService = Executors.newScheduledThreadPool(1)
    }

    var testMessageSendTask: ScheduledFuture<*>? = null

    fun startTestMessageSender() {
        stopTestMessageSender()
        testMessageSendTask = ex.scheduleAtFixedRate({
            ctx.send(
                ServerMessage(
                    "chatcommand", ChatMessage(
                        PlayerId.SYSTEM_ID,
                        "${System.currentTimeMillis()}"
                    )
                )
            )
        }, 1L, 1L, TimeUnit.SECONDS)
    }

    fun stopTestMessageSender() {
        testMessageSendTask?.cancel(true)
        testMessageSendTask = null
    }

    override fun close() {
        testMessageSendTask?.cancel(true)
    }
}
