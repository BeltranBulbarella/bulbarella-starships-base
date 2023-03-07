package starships.model

import starships.enums.KeyMovement
import starships.enums.State
import starships.`interface`.Collidable

class GameState(
    val gameWidth: Double,
    val gameHeight: Double,
    val gameObjects: List<Collidable>,
    val state: State
) {

    fun moveShip(id: String, movement: KeyMovement, secondsPassed: Double): GameState {
        //if game is paused, return the same game state
        if(state == State.PAUSE) {
            return GameState(gameWidth, gameHeight, gameObjects, state)
        }
        val ship = gameObjects.find { it.getId() == id} ?: throw Error("ship with id $id not found")
        val remainingObjects = gameObjects.filter { it.getId() != id}
        return when (ship) {
            //if the ship is found, do the movement
            is Starship -> doMovement(ship, secondsPassed, movement, remainingObjects)
            //if the ship is not found, return the same game state
            else -> GameState(gameWidth, gameHeight, gameObjects, state)
        }
    }

    private fun doMovement(ship: Starship, secondsPassed: Double, movement: KeyMovement, remainingObjects: List<Collidable>): GameState {
        return when(movement) {
            //given a key pressed return a gamestate with the move applied
            KeyMovement.TURN_RIGHT -> GameState(gameWidth, gameHeight, remainingObjects.plus(ship.turnRight(secondsPassed)), state)
            KeyMovement.TURN_LEFT -> GameState(gameWidth, gameHeight, remainingObjects.plus(ship.turnLeft(secondsPassed)), state)
            KeyMovement.ACCELERATE -> GameState(gameWidth, gameHeight, remainingObjects.plus(ship.accelerate()), state)
            KeyMovement.STOP -> GameState(gameWidth, gameHeight, remainingObjects.plus(ship.decelerate()), state)
            KeyMovement.SHOOT -> GameState(gameWidth, gameHeight, gameObjects.plus(ship.shoot()), state)
        }
    }

    fun toggleState(): GameState {
        val newState = if(state == State.PAUSE) {
            State.RUN
        }else {
            State.PAUSE
        }
        return GameState(gameWidth, gameHeight, gameObjects, newState)
    }

    fun move(secondsPassed: Double): GameState {
        if(state == State.PAUSE) {
            return GameState(gameWidth, gameHeight, gameObjects, state)
        }
        return GameState(gameWidth, gameHeight, manageGameObjects(secondsPassed), state)
    }

    private fun manageGameObjects(secondsPassed: Double): List<Collidable> {
        //when out of bounds remove the object
        val newObjects = gameObjects
            .map { it.move(secondsPassed, gameWidth, gameHeight) }.filter { isOutOfBounds(it) }
        return if(Math.random()*100 < 99.5) {
            //on each move 99.5% of the time, return the same list of objects
            newObjects
        } else {
            //else add a new asteroid
            newObjects.plus(Asteroid.new(gameWidth, gameHeight))
        }
    }

    private fun isOutOfBounds(it: Collidable): Boolean {
        //check if is out of bounds
        return it.getPosition().getX() < gameWidth && it.getPosition().getY() < gameHeight && it.getPosition().getX() > 0.0 && it.getPosition().getY() > 0
    }

    fun collision(from: String, to: String): GameState {
        //on collision find both objects and if after de collision they were not destroyed, return the same game state
        var remainingObjects = gameObjects.filter { it.getId() != from && it.getId() != to}
        val fromObj = gameObjects.find { it.getId() == from }
        val toObj = gameObjects.find { it.getId() == to }
        if(fromObj == null || toObj == null) return this
        val c1 = fromObj.collide(toObj)
        val c2 = toObj.collide(fromObj)
        if(c1.isPresent) remainingObjects = remainingObjects.plus(c1.get())
        if(c2.isPresent) remainingObjects = remainingObjects.plus(c2.get())
        return GameState(gameWidth, gameHeight, remainingObjects, state)
    }


}
