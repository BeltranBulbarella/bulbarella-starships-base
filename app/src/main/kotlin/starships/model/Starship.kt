package starships.model

import starships.`interface`.Collidable
import starships.`interface`.Weapon
import java.lang.Math.cos
import java.lang.Math.sin
import java.util.*

class Starship(
    private val id: String,
    val remainingLives: Int,
    private val position: Position,
    private val vector: Vector,
    private val weapon: Weapon,
) : Collidable {
    override fun move(secondsPassed: Double, gameWidth: Double, gameHeight: Double): Collidable {
        val xPosition =
            position.getX() + (vector.speed * -sin(Math.toRadians(vector.rotationInDegrees)) * secondsPassed * 150)
        val yPosition =
            position.getY() + vector.speed * cos(Math.toRadians(vector.rotationInDegrees)) * secondsPassed * 150

        //if move outside of bounds then move to random position
        val newPosition = if (xPosition < gameWidth && xPosition > 0 && yPosition < gameHeight && yPosition > 0) {
            Position(xPosition, yPosition)
        } else {
            Position(Math.random() * gameWidth, Math.random() * gameHeight)
        }

        return Starship(id, remainingLives, newPosition, vector, weapon)
    }

    override fun getId(): String {
        return id
    }

    override fun getPosition(): Position {
        return position
    }

    override fun getVector(): Vector {
        return vector
    }

    override fun collide(collidable: Collidable): Optional<Collidable> {
        when (collidable) {
            //if bullet then lose a life
            is Bullet -> {
                if (remainingLives - 1 <= 0) return Optional.empty<Collidable>()
                return Optional.of(Starship(id, remainingLives - 1, position, vector, weapon))
            }
            //if asteroid then lose a life and move to random position
            is Asteroid -> {
                if (remainingLives - 1 <= 0) return Optional.empty<Collidable>()
                return Optional.of(
                    Starship(
                        id,
                        remainingLives - 1,
                        Position(position.getX() + 40, position.getY() + 40),
                        vector,
                        weapon
                    )
                )
            }

            is Starship -> {
                //if starship then remove a life and move to random position
                if (remainingLives - 1 <= 0) return Optional.empty<Collidable>()
                return Optional.of(
                    Starship(
                        id,
                        remainingLives - 1,
                        Position(position.getX() + 20, position.getY() + 20),
                        vector,
                        weapon
                    )
                )
            }

            else -> {
                return Optional.empty<Collidable>()
            }
        }
    }

    fun turnLeft(secondsPassed: Double): Starship =
        //to move left we simply subtract from the rotation
        Starship(
            id, remainingLives, position, Vector(vector.rotationInDegrees - (200 * secondsPassed), vector.speed), weapon
        )

    fun turnRight(secondsPassed: Double): Starship =
        //to move right we simply add to the rotation
        Starship(
            id, remainingLives, position, Vector(vector.rotationInDegrees + (200 * secondsPassed), vector.speed), weapon
        )

    fun accelerate(): Starship {
        //to accelerate we simply add to the speed
        val newSpeed = if (vector.speed == 1.0) vector.speed else vector.speed + 1
        return Starship(id, remainingLives, position, Vector(vector.rotationInDegrees, newSpeed), weapon)
    }

    fun decelerate(): Starship {
        //to decelerate we simply subtract from the speed
        val newSpeed = if (vector.speed == -1.0) vector.speed else vector.speed - 1
        return Starship(id, remainingLives, position, Vector(vector.rotationInDegrees, newSpeed), weapon)
    }

    fun shoot(): List<Bullet> {
        return weapon.shoot(position, vector)
    }


}