package de.groovybyte.gamejam.styxreactor.game.datatransfer.exceptions


class InvalidPayloadException(val payload: Any?, throwable: Throwable? = null) :
    IllegalArgumentException("Invalid payload", throwable)
