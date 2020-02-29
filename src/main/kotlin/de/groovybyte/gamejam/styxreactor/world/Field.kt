package de.groovybyte.gamejam.styxreactor.world

import de.groovybyte.gamejam.styxreactor.texture.TextureId
import de.groovybyte.gamejam.styxreactor.texture.Textured

sealed class Field(
    val type: FieldType
) : Textured {

    enum class FieldType {
        /**
         * field for players
         */
        ACCESSIBLE,
//        HOLE,
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
    ) : Field(FieldType.ACCESSIBLE)

    data class WallField(
        override val textureId: TextureId
    ) : Field(FieldType.WALL)
}
