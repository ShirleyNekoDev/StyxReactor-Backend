package de.groovybyte.gamejam.styxreactor.fixtures

import de.groovybyte.gamejam.styxreactor.game.world.Field
import de.groovybyte.gamejam.styxreactor.game.world.World
import kotlin.random.Random

val WORLD_1 = World.generate(10, 10) { x, y ->
    when(Random.nextInt(10)) {
        in 0..0 -> Field.WallField.DEFAULT
        in 0..3 -> Field.AccessibleField.STEALTH
        else -> Field.AccessibleField.DEFAULT
    }
}
