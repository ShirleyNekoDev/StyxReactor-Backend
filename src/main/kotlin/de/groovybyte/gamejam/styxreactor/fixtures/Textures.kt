package de.groovybyte.gamejam.styxreactor.fixtures

object Textures {
    enum class Field : IdString {
        EMPTY,
        SMOKE,
        WALL,
        HOLE
    }
}

interface IdString {
    val name: String

    val id: String
        get() = "${this::class.qualifiedName!!.substringAfter("fixtures.")}.$name"
            .replace('.', ':')
            .toLowerCase()
}
