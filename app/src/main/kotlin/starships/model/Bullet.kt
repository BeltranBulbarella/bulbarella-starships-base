package starships.model

import starships.`interface`.Collidable
import java.util.Optional
import kotlin.math.cos
import kotlin.math.sin

class Bullet(
    private val id: String,
    private val position: Position,
    private val vector: Vector,
    private val damage: Double
) : Collidable {

    override fun move(secondsPassed: Double, gameWidth: Double, gameHeight: Double): Bullet {
        //moves the same way as the asteroid
        val newPosition = Position(
            position.getX() + vector.speed * -sin(Math.toRadians(vector.rotationInDegrees)) * secondsPassed * 150,
            position.getY() + vector.speed * cos(Math.toRadians(vector.rotationInDegrees)) * secondsPassed * 150
        )
        return Bullet(id, newPosition, vector, damage)
    }

    override fun getId(): String = id

    override fun getPosition(): Position = position

    override fun getVector(): Vector = vector

    override fun collide(collidable: Collidable): Optional<Collidable> {
        return Optional.empty<Collidable>()
    }

    fun getDamage(): Double = damage

}