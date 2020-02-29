package de.groovybyte.gamejam.styxreactor.game.state

import de.groovybyte.gamejam.styxreactor.game.ClientId
import de.groovybyte.gamejam.styxreactor.game.player.Player
import de.groovybyte.gamejam.styxreactor.game.player.PlayerId

class MatchSession {
    val connectedClients: MutableMap<ClientId, PlayerId> = HashMap()
    val connectedPlayers: MutableMap<PlayerId, Player> = HashMap()
}
