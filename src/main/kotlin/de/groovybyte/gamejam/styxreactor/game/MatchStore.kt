package de.groovybyte.gamejam.styxreactor.game

import de.groovybyte.gamejam.styxreactor.framework.ClientContext
import de.groovybyte.gamejam.styxreactor.game.state.MatchSession

object MatchStore {
    private val SINGLETON_MATCH: MatchSession = MatchSession()
    fun getSession(ctx: ClientContext): MatchSession = SINGLETON_MATCH
}
