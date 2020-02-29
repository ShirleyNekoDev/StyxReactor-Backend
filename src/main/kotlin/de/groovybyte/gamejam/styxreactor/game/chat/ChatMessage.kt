package de.groovybyte.gamejam.styxreactor.game.chat

import de.groovybyte.gamejam.styxreactor.game.datatransfer.Entity
import de.groovybyte.gamejam.styxreactor.game.player.PlayerId

data class ChatMessage(
    val author: PlayerId,
    val message: String
) : Entity<ChatMessage>
