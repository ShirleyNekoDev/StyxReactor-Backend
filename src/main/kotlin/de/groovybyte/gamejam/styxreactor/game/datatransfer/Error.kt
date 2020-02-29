package de.groovybyte.gamejam.styxreactor.game.datatransfer

import de.groovybyte.gamejam.styxreactor.framework.ClientContext
import de.groovybyte.gamejam.styxreactor.game.datatransfer.server2client.ServerMessage

class Error(
    val type: String,
    val data: Any? = null
)

fun ClientContext.sendError(type: String, data: Any? = null) = send(
    ServerMessage(
        command = "error",
        payload = Error(
            type = type,
            data = data
        )
    )
)
