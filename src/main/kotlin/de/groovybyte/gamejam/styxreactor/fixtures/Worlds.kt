package de.groovybyte.gamejam.styxreactor.fixtures

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.groovybyte.gamejam.styxreactor.game.world.Field
import de.groovybyte.gamejam.styxreactor.game.world.World
import kotlin.random.Random

object Worlds {
    fun random() = World.generate(10, 10) { x, y ->
        when(Random.nextInt(10)) {
            in 0..1 -> Field.WallField.DEFAULT
            in 1..2 -> Field.HoleField.DEFAULT
            in 2..4 -> Field.AccessibleField.STEALTH
            else -> Field.AccessibleField.DEFAULT
        }
    }

    private val worldCache: MutableMap<String, World> = HashMap()
    operator fun get(name: String): World = worldCache.getOrPut(name) {
        World.load(jacksonObjectMapper(), "resources/worlds/$name.json")
    }
}
