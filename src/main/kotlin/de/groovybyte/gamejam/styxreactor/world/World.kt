package de.groovybyte.gamejam.styxreactor.world

import de.groovybyte.gamejam.styxreactor.datatransfer.Entity

class World(
    val width: Int,
    val height: Int,
    val grid: List<Field>
) : Entity<World> {
    fun getField(x: Int, y: Int): Field {
        if (!(x in 0..width && y in 0..height)) {
            throw IndexOutOfBoundsException()
        }
        return grid[x + width * y]
    }
}
