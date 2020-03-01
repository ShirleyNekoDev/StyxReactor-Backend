package de.groovybyte.gamejam.styxreactor.game.world

import de.groovybyte.gamejam.styxreactor.fixtures.Worlds
import de.groovybyte.gamejam.styxreactor.game.datatransfer.server2client.ServerMessage
import de.groovybyte.gamejam.styxreactor.framework.ClientContext

object WorldController {
    fun sendWorld(ctx: ClientContext) {
        ctx.send(ServerMessage("set_world", Worlds["Hyperforge"]))
    }
}
