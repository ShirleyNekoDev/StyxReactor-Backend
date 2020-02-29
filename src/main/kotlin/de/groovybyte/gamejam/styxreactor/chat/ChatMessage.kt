package de.groovybyte.gamejam.styxreactor.chat

import de.groovybyte.gamejam.styxreactor.datatransfer.Entity
import de.groovybyte.gamejam.styxreactor.player.PlayerId

data class ChatMessage(
    val author: PlayerId,
    val message: String
) : Entity<ChatMessage>
