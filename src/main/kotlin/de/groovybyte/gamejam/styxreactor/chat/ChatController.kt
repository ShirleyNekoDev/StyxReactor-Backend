package de.groovybyte.gamejam.styxreactor.chat

import de.groovybyte.gamejam.styxreactor.datatransfer.GameSession
import de.groovybyte.gamejam.styxreactor.datatransfer.Message
import de.groovybyte.gamejam.styxreactor.utils.WebSocketController

object ChatController {
    fun receiveMessage(ctx: WebSocketController<GameSession>.ClientContext, msg: Message<ChatMessage>) {
        val message: String = msg.payload.message
    }
}
