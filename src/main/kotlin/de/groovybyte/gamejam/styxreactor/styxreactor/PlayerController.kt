package de.groovybyte.gamejam.styxreactor.styxreactor

import de.groovybyte.gamejam.styxreactor.utils.WebSocketController
import io.jooby.Context
import io.jooby.WebSocketCloseStatus
import io.jooby.WebSocketMessage
import io.jooby.exception.MissingValueException
import org.slf4j.Logger
import java.util.*

class PlayerController(log: Logger) : WebSocketController<Player>(log) {
    private val players: MutableMap<UUID, Player> = HashMap()

    override fun initialPayload(ctx: Context): Player {
        val playerId = try {
            ctx.query("playerId").value { UUID.fromString(it) }
        } catch (_: MissingValueException) {
            UUID.randomUUID()
        }

        return players.getOrPut(playerId) {
            Player(id = playerId).apply {
                username = "Player ${players.size + 1}"
            }
        }
    }

    override fun onConnect(
        ctx: ClientContext
    ) {
        log.info("[{}] took {} [{}]", ctx.id, ctx.payload.username, ctx.payload.id)
        ctx.broadcast("[${ctx.payload.username}] connected", false)
        ctx.send("[${ctx.payload.username}] successfully connected")
        if (ctx.others.isNotEmpty()) {
            val otherPlayerNames = ctx.others.joinToString { it.payload.username }
            ctx.send("[$otherPlayerNames] already connected")
        }
    }

    override fun onMessage(
        ctx: ClientContext,
        message: WebSocketMessage
    ) {
        ctx.broadcast("[${ctx.payload.username}] >> ${message.value()}")
    }

    override fun onClose(
        ctx: ClientContext,
        closeStatus: WebSocketCloseStatus
    ) {
        ctx.broadcast("[${ctx.payload.username}] disconnected (cause=${closeStatus.reason})", false)
    }
}
