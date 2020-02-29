package de.groovybyte.gamejam.styxreactor.game.world

import de.groovybyte.gamejam.styxreactor.fixtures.Textures
import de.groovybyte.gamejam.styxreactor.game.texture.TextureId
import de.groovybyte.gamejam.styxreactor.game.texture.Textured

sealed class Field(
    val type: FieldType
) : Textured {

    enum class FieldType {
        /**
         * field for players
         */
        ACCESSIBLE,
        HOLE,
//        /**
//         * has a collider for filling the field
//         */
//        OBJECT,
        /**
         * special case of object (fills whole field)
         */
        WALL
    }

    data class AccessibleField(
        val isStealth: Boolean,
        override val textureId: TextureId
    ) : Field(FieldType.ACCESSIBLE) {
        companion object {
            val DEFAULT = AccessibleField(
                textureId = Textures.Field.EMPTY.id,
                isStealth = false
            )
            val STEALTH = AccessibleField(
                textureId = Textures.Field.SMOKE.id,
                isStealth = true
            )
        }
    }

    data class WallField(
        override val textureId: TextureId
    ) : Field(FieldType.WALL) {
        companion object {
            val DEFAULT = WallField(
                textureId = Textures.Field.WALL.id
            )
        }
    }

    data class HoleField(
        override val textureId: TextureId
    ) : Field(FieldType.HOLE) {
        companion object {
            val DEFAULT = HoleField(
                textureId = Textures.Field.HOLE.id
            )
        }
    }
}
