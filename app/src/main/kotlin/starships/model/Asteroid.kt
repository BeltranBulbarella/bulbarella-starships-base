package starships.model

import starships.`interface`.Collidable
import java.lang.Math.cos
import java.lang.Math.sin
import java.util.*


class Asteroid(
    private val id: String,
    private val position: Position,
    private val vector: Vector,
    val health: Double,
    private val size: Double
) : Collidable {

    companion object Factory {
        fun new(gameWidth: Double, gameHeight: Double): Asteroid {
            //creates a new asteroid with a random position and a random vector
            //unique id and 20 health
            return Asteroid(
                UUID.randomUUID().toString(),
                Position(Math.random() * gameWidth, Math.random() * gameHeight),
                Vector(Math.random() * 359, 0.6),
                20.0,
                50.0
            )
        }
    }

    override fun move(secondsPassed: Double, gameWidth: Double, gameHeight: Double): Collidable {
        //moves the asteroid in the direction of the vector
        val xPosition =
            position.getX() + (vector.speed * -sin(Math.toRadians(vector.rotationInDegrees)) * secondsPassed * 150)
        val yPosition =
            position.getY() + vector.speed * cos(Math.toRadians(vector.rotationInDegrees)) * secondsPassed * 150
        return Asteroid(id, Position(xPosition, yPosition), vector, health, size)
    }

    override fun getId(): String = id

    override fun getPosition(): Position = position

    override fun getVector(): Vector = vector

    override fun collide(collidable: Collidable): Optional<Collidable> {
        return when (collidable) {
            //if the asteroid collides with a bullet, it loses health
            is Bullet -> {
                if (health - collidable.getDamage() <= 0) return Optional.empty<Collidable>()
                return Optional.of(
                    Asteroid(
                        id,
                        position,
                        vector,
                        health - collidable.getDamage(),
                        size - collidable.getDamage()
                    )
                )
            }
            //if the asteroid collides with another asteroid, it does nothing
            is Asteroid -> Optional.of(Asteroid(id, position, vector, health, size))
            //if the asteroid collides with a ship, it does nothing
            is Starship -> Optional.of(Asteroid(id, position, vector, health, size))
            else -> {
                Optional.empty<Collidable>()
            }
        }
    }


}