package uk.j4numbers.adventofcode.advent_2024.day_18.pojo

class PathNode constructor(
    val coordinate: Coordinate,
    val neighbours: List<Coordinate>,
    var scanned: Boolean = false,
    var score: Int = Int.MAX_VALUE,
) {
    override fun toString(): String {
        return String.format("PathNode[$coordinate,[$neighbours],$scanned,$score]");
    }
}
