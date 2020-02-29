package de.groovybyte.gamejam.styxreactor.datatransfer

interface Entity<E : Entity<E>> {
    val type: String get() = this::class.simpleName!!.toLowerCase()
}
