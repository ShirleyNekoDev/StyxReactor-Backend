package de.groovybyte.gamejam.styxreactor.game.datatransfer.utils

import de.groovybyte.gamejam.styxreactor.game.datatransfer.Entity
import de.groovybyte.gamejam.styxreactor.game.datatransfer.client2server.ClientMessage


class Echo(val data: String) : Entity<Echo>

fun ClientMessage.asEcho() = Echo(packetData)
