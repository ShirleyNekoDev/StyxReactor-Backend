package de.groovybyte.gamejam.styxreactor.world

import de.groovybyte.gamejam.styxreactor.datatransfer.GameSession
import de.groovybyte.gamejam.styxreactor.datatransfer.server2client.ServerMessage
import de.groovybyte.gamejam.styxreactor.fixtures.WORLD_1
import de.groovybyte.gamejam.styxreactor.utils.WebSocketController

object WorldController {
    fun sendWorld(ctx: WebSocketController<GameSession>.ClientContext) {
        ctx.send(ServerMessage("set_world", WORLD_1))
    }
}
