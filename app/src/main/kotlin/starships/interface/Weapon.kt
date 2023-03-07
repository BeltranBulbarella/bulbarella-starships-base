package starships.`interface`

import starships.model.Bullet
import starships.model.Position
import starships.model.Vector

interface Weapon {
    fun shoot(shipPosition: Position, shipVector: Vector): List<Bullet>
}