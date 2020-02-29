package de.groovybyte.gamejam.styxreactor.datatransfer.exceptions


class InvalidPayloadException(val payload: Any?, throwable: Throwable? = null) :
    IllegalArgumentException("Invalid payload", throwable)
