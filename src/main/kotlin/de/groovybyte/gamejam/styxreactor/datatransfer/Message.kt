package de.groovybyte.gamejam.styxreactor.datatransfer

class Message<Type : Entity<Type>>(
    val command: String,
    val payload: Type
)
