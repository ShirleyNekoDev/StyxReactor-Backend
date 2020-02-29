package de.groovybyte.gamejam.styxreactor.world

object WorldFactory {
    private val WALL_FIELD = Field.WallField(
        textureId = "texture:field:wall"
    )

    private val EMPTY_FIELD = Field.AccessibleField(
        isStealth = false,
        textureId = "texture:field:empty"
    )

    fun generateWorld(width: Int, height: Int): World {
        val grid = List(width * height) { index ->
            val x = index % width
            val y = index / width
            if (x == 0 || x == width - 1 || y == 0 || y == height - 1) {
                WALL_FIELD.copy()
            } else {
                EMPTY_FIELD.copy()
            }
        }
        return World(
            width = width,
            height = height,
            grid = grid
        )
    }
}
