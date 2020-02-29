package de.groovybyte.gamejam.styxreactor.game.state

import de.groovybyte.gamejam.styxreactor.framework.ClientContext
import de.groovybyte.gamejam.styxreactor.game.chat.ChatMessage
import de.groovybyte.gamejam.styxreactor.game.datatransfer.server2client.ServerMessage
import de.groovybyte.gamejam.styxreactor.game.player.PlayerId
import java.io.Closeable
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class ClientSession(val ctx: ClientContext) : Closeable {
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
