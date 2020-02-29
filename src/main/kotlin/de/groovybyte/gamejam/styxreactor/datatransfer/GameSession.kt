package de.groovybyte.gamejam.styxreactor.datatransfer

import de.groovybyte.gamejam.styxreactor.chat.ChatMessage
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

    lateinit var testMessageSendTask: ScheduledFuture<*>

    fun startTestMessageSender() {
        testMessageSendTask = ex.scheduleAtFixedRate({
            ctx.send(
                Message(
                    "chatcommand", ChatMessage(
                        PlayerId.SYSTEM_ID,
                        "${System.currentTimeMillis()}"
                    )
                )
            )
        }, 1L, 1L, TimeUnit.SECONDS)
    }

    override fun close() {
        testMessageSendTask.cancel(true)
    }
}
