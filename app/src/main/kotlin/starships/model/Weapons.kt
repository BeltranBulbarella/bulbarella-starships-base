package starships.model

import starships.enums.BulletType
import starships.`interface`.Weapon
import java.lang.Math.cos
import java.lang.Math.sin
import java.util.*


class Weapons(private val bulletType: BulletType) : Weapon {

//    2 types of weapons. A double shoot and a single shoot
    override fun shoot(shipPosition: Position, shipVector: starships.model.Vector): List<Bullet> {
        if (bulletType.toString() === BulletType.COMMON.toString()) {
            return listOf(
                Bullet(
                    UUID.randomUUID().toString(),
                    Position(
                        shipPosition.getX() + 50 * -sin(Math.toRadians(shipVector.rotationInDegrees)),
                        shipPosition.getY() + 50 * cos(Math.toRadians(shipVector.rotationInDegrees))
                    ),
                    Vector(shipVector.rotationInDegrees, 3.0),
                    10.0

                )
            )
        } else {
            return listOf(
                Bullet(
                    UUID.randomUUID().toString(),
                    Position(
                        shipPosition.getX() + 50 * -sin(Math.toRadians(shipVector.rotationInDegrees)),
                        shipPosition.getY() + 50 * cos(Math.toRadians(shipVector.rotationInDegrees))
                    ),
                    Vector(shipVector.rotationInDegrees, 3.0),
                    5.0
                ),
                Bullet(
                    UUID.randomUUID().toString(),
                    Position(
                        shipPosition.getX() + 100 * -sin(Math.toRadians(shipVector.rotationInDegrees)),
                        shipPosition.getY() + 100 * cos(Math.toRadians(shipVector.rotationInDegrees))
                    ),
                    Vector(shipVector.rotationInDegrees, 3.0),
                    5.0
                ),
            )
        }

    }
}