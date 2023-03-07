
import edu.austral.ingsis.starships.ui.ElementColliderType
import edu.austral.ingsis.starships.ui.ElementModel
import edu.austral.ingsis.starships.ui.ImageRef
import javafx.scene.control.Label
import javafx.scene.input.KeyCode
import starships.enums.KeyMenuAction
import starships.enums.KeyMovement
import starships.enums.MovementType
import starships.`interface`.Collidable
import starships.model.*

class GameManager(
    var gameState: GameState,
    private val playerKeyMap: Map<KeyCode, ShipMovement>,
    private val gameKeyMap: Map<KeyCode, MenuAction>,
    private val keysPressed: MutableList<KeyCode>
    ) {
//para hacer move llamo al magager y este llama al game state
    private fun moveShip(movement: KeyCode, secondsPassed: Double) {
        val shipMovement = playerKeyMap.getValue(movement)
        gameState = gameState.moveShip(shipMovement.id, shipMovement.movement, secondsPassed)
    }

    //returns the game state with the time passed
    fun passTime(secondsPassed: Double, elements: MutableMap<String, ElementModel>) {
      keysPressed.forEach {moveShip(it, secondsPassed)}
      val newGameState = gameState.move(secondsPassed)
      val removedGameObjects = gameState.gameObjects.filter { obj -> !newGameState.gameObjects.any { newObj -> newObj.getId() == obj.getId()} }
      removedGameObjects.forEach {elements.remove(it.getId()) }
      gameState = newGameState
    }

    fun addElements(elements: MutableMap<String, ElementModel>) {
        val newElements = gameState.gameObjects.filter { !elements.keys.contains(it.getId()) }
        newElements.forEach { elements[it.getId()] = elementToUI(it) }
    }

    fun collision(from: String, to: String,  elements: MutableMap<String, ElementModel>) {
        val newGameState = gameState.collision(from, to)
        //chequea colisiones
        //devuelve un game state nuevo sacando los que murieron
        val removedGameObjects = gameState.gameObjects.filter { obj -> !newGameState.gameObjects.any { newObj -> newObj.getId() == obj.getId()} }
        removedGameObjects.forEach { elements.remove(it.getId()) }
        gameState = newGameState
    }

    fun elementToUI(element: Collidable): ElementModel {
        return when (element) {
            is Starship -> starshipToStarshipUI(element)
            is Asteroid -> asteroidToAsteroidUI(element)
            is Bullet -> bulletToBulletUI(element)
            else -> {
                throw Exception("Unknown element type")
            }
        }
    }

    private fun starshipToStarshipUI(ship: Starship): ElementModel {
        return ElementModel(
            ship.getId(),
            ship.getPosition().getX(),
            ship.getPosition().getY(),
            40.0,
            40.0,
            ship.getVector().rotationInDegrees,
            ElementColliderType.Triangular,
            ImageRef("starship", 60.0, 70.0)
        )
    }

    private fun asteroidToAsteroidUI(asteroid: Asteroid): ElementModel {
        return ElementModel(
            asteroid.getId(),
            asteroid.getPosition().getX(),
            asteroid.getPosition().getY(),
            100.0,
            100.0,
            asteroid.getVector().rotationInDegrees,
            ElementColliderType.Elliptical,
            ImageRef("asteroid", 60.0, 70.0)

        )
    }

    private fun bulletToBulletUI(bullet: Bullet): ElementModel {
        return ElementModel(
            bullet.getId(),
            bullet.getPosition().getX(),
            bullet.getPosition().getY(),
            20.0,
            20.0,
            bullet.getVector().rotationInDegrees,
            ElementColliderType.Rectangular,
            ImageRef("bullet", 60.0, 70.0)
        )
    }

    fun pressKey(key: KeyCode) {
        when (val action = if (playerKeyMap.containsKey(key)) { playerKeyMap.getValue(key) } else { gameKeyMap.getValue(key) }) {
            //on press key se fija si es un movimiento o una accion de menu
            is ShipMovement -> manageShipMovement(key)
            is MenuAction -> manageMenuAction(action)
        }
    }

    private fun manageShipMovement(key: KeyCode) {
        when (playerKeyMap.getValue(key).movementType) {
            //on ship move pass id and 1 sec passed
            MovementType.CLICK -> moveShip(key, 1.0)
            MovementType.MAINTAIN -> if(!keysPressed.contains(key)) { keysPressed.add(key) }
        }
    }

    private fun manageMenuAction(menuAction: MenuAction) {
        when (menuAction.action) {
            KeyMenuAction.TOGGLE_PAUSE -> gameState = gameState.toggleState()
        }
    }

    fun releaseKey(key: KeyCode) {
        keysPressed.remove(key)
    }

    fun updateLives(id: String): Label {
        val ship = gameState.gameObjects.find {
            it.getId() == id
        }
        return when (ship) {
            is Starship -> Label(ship.remainingLives.toString())
            else -> return Label("")
        }
    }
}

class ShipMovement(val id: String, val movement: KeyMovement, val movementType: MovementType)
class MenuAction(val action: KeyMenuAction)

