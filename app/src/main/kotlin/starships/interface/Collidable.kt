package starships.`interface`

import starships.model.Position
import starships.model.Vector
import java.util.Optional

interface Collidable {
    fun move(secondsPassed: Double, gameWidth: Double, gameHeight: Double): Collidable
    fun getId(): String
    fun getPosition(): Position
    fun getVector(): Vector
    fun collide(collidable: Collidable): Optional<Collidable>
}