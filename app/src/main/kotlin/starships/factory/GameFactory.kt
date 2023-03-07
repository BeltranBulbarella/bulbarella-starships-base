package starships.factory

import GameManager
import MenuAction
import ShipMovement
import javafx.scene.input.KeyCode
import starships.enums.*
import starships.model.*

class GameFactory {

    //returns a game with the starship game state and keycodes

    fun singlePlayerGame(): GameManager {
        val gameShip = Starship(
            "starship1",
            3,
            Position(450.0, 450.0),
            Vector(350.0, 0.0),
            Weapons(BulletType.COMMON)
        )
        val gameState = GameState(
            800.0,
            800.0,
            listOf(gameShip),
            State.PAUSE
        )

        return GameManager(
            gameState,
            mapOf(
                Pair(KeyCode.UP, ShipMovement(gameShip.getId(), KeyMovement.ACCELERATE, MovementType.CLICK)),
                Pair(KeyCode.DOWN, ShipMovement(gameShip.getId(), KeyMovement.STOP, MovementType.CLICK)),
                Pair(KeyCode.LEFT, ShipMovement(gameShip.getId(), KeyMovement.TURN_LEFT, MovementType.MAINTAIN)),
                Pair(KeyCode.RIGHT, ShipMovement(gameShip.getId(), KeyMovement.TURN_RIGHT, MovementType.MAINTAIN)),
                Pair(KeyCode.SPACE, ShipMovement(gameShip.getId(), KeyMovement.SHOOT, MovementType.CLICK)),
            ),
            mapOf(
                Pair(KeyCode.P, MenuAction(KeyMenuAction.TOGGLE_PAUSE))
            ),
            mutableListOf()
        );
    }

    fun twoPlayersGame(): GameManager {
        val gameShip = Starship(
            "starship1",
            3,
            Position(450.0, 450.0),
            Vector(350.0, 2.0),
            Weapons(BulletType.COMMON)
        )
        val gameShip2 = Starship(
            "starship2",
            3,
            Position(100.0, 100.0),
            Vector(350.0, 0.0),
            Weapons(BulletType.SPECIAL)
        )

        val gameState = GameState(
            800.0,
            800.0,
            listOf(gameShip, gameShip2),
            State.PAUSE
        )

        return GameManager(
            gameState,
            mapOf(
                Pair(KeyCode.UP, ShipMovement(gameShip.getId(), KeyMovement.ACCELERATE, MovementType.CLICK)),
                Pair(KeyCode.DOWN, ShipMovement(gameShip.getId(), KeyMovement.STOP, MovementType.CLICK)),
                Pair(KeyCode.LEFT, ShipMovement(gameShip.getId(), KeyMovement.TURN_LEFT, MovementType.MAINTAIN)),
                Pair(KeyCode.RIGHT, ShipMovement(gameShip.getId(), KeyMovement.TURN_RIGHT, MovementType.MAINTAIN)),
                Pair(KeyCode.SPACE, ShipMovement(gameShip.getId(), KeyMovement.SHOOT, MovementType.CLICK)),
                Pair(KeyCode.W, ShipMovement(gameShip2.getId(), KeyMovement.ACCELERATE, MovementType.CLICK)),
                Pair(KeyCode.S, ShipMovement(gameShip2.getId(), KeyMovement.STOP, MovementType.CLICK)),
                Pair(KeyCode.A, ShipMovement(gameShip2.getId(), KeyMovement.TURN_LEFT, MovementType.MAINTAIN)),
                Pair(KeyCode.D, ShipMovement(gameShip2.getId(), KeyMovement.TURN_RIGHT, MovementType.MAINTAIN)),
                Pair(KeyCode.E, ShipMovement(gameShip2.getId(), KeyMovement.SHOOT, MovementType.CLICK)),
            ),
            mapOf(
                Pair(KeyCode.P, MenuAction(KeyMenuAction.TOGGLE_PAUSE))
            ),
            mutableListOf()
        );
    }
}