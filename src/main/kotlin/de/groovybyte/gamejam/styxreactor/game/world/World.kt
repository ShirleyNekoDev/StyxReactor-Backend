package de.groovybyte.gamejam.styxreactor.game.world

import de.groovybyte.gamejam.styxreactor.game.datatransfer.Entity

data class World(
    val width: Int,
    val height: Int,
    val grid: Array<Field>
) : Entity<World> {
    companion object {
        fun generate(innerWidth: Int, innerHeight: Int, fieldGenerator: (x: Int, y: Int) -> Field): World {
            val width = innerWidth + 2
            val height = innerHeight + 2
            return World(
                width = width,
                height = height,
                grid = Array(width * height) { index ->
                    val x = index % width
                    val y = index / width
                    if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                        Field.WallField.DEFAULT
                    } else {
                        fieldGenerator(x - 1, y - 1)
                    }
                }
            )
        }

        /*

    private val EMPTY_FIELD = Field.AccessibleField(
        isStealth = false,
        textureId = "texture:field:empty"
    )

    private fun World.generateBorderWalls(): World = copy(
        width = width + 2,
        height = height + 2,
        grid = List(width * height) { index ->
            val x = index % width
            val y = index / width
            wallField.takeIf { x == 0 || x == width - 1 || y == 0 || y == height - 1 }
        }
    )

    private fun generateWorldBorderWalls(innerWidth: Int, innerHeight: Int, wallField: Field): List<Field?> {
        val width = innerWidth + 2
        val height = innerHeight + 2
        return List(width * height) { index ->
            val x = index % width
            val y = index / width
            wallField.takeIf { x == 0 || x == width - 1 || y == 0 || y == height - 1 }
        }
    }

    fun generateWorld(innerWidth: Int, innerHeight: Int, fieldGenerator: (x: Int, y: Int) -> Field): World {
        val grid = generateWorldBorderWalls(i)
        return World(
            width = width,
            height = height,
            grid = grid
        )
    }
         */
    }

    fun getField(x: Int, y: Int): Field {
        if (!(x in 0..width && y in 0..height)) {
            throw IndexOutOfBoundsException()
        }
        return grid[x + width * y]
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as World

        if (width != other.width) return false
        if (height != other.height) return false
        if (!grid.contentEquals(other.grid)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        result = 31 * result + grid.contentHashCode()
        return result
    }
}
