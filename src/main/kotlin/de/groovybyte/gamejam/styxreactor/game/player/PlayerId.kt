package de.groovybyte.gamejam.styxreactor.game.player

import java.util.*

inline class PlayerId(val value: String) {
    companion object {
        fun random(): PlayerId = PlayerId(UUID.randomUUID().toString())

        val SYSTEM_ID = PlayerId("server")
    }
}
