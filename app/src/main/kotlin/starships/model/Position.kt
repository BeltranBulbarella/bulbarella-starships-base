package starships.model

data class Position(
    private val x: Double,
    private val y: Double
){
    fun getX(): Double {
        return x
    }
    fun getY(): Double {
        return y
    }
}