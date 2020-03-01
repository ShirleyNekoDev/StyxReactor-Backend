package de.groovybyte.gamejam.styxreactor.game.ping

import de.groovybyte.gamejam.styxreactor.game.world.FieldPosition

data class PingMessage(
    val position: FieldPosition,
    val type: String,
    val player: String
)
