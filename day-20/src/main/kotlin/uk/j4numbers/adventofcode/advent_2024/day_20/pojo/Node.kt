package uk.j4numbers.adventofcode.advent_2024.day_20.pojo

class Node constructor(
    val coordinate: Coordinate,
    val type: NodeType,
    var score: Int = Int.MAX_VALUE,
    var scanned: Boolean = false,
    // Split into distance and the actual coordinate - will be 1 for direct neighbours
    private val neighbours: MutableList<Pair<Int, Coordinate>> = mutableListOf(),
) {
    fun getNeighbours(): List<Pair<Int, Coordinate>> {
        return this.neighbours;
    }

    fun addNeighbour(neighbourToAdd: Pair<Int, Coordinate>) {
        this.neighbours.add(neighbourToAdd);
    }

    override fun toString(): String {
        return "Node(coordinate=$coordinate, type=$type, neighbours=$neighbours, score=$score)"
    }
}
