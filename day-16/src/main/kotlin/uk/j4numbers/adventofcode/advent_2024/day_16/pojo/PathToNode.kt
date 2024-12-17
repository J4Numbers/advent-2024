package uk.j4numbers.adventofcode.advent_2024.day_16.pojo

class PathToNode constructor(
    val source: Coordinate,
    val direction: Direction,
    var destination: Coordinate = Coordinate(-1, -1),
    var approach: Direction = Direction.UNKNOWN,
    var length: Int = Int.MAX_VALUE,
    var startScore: Int = Int.MAX_VALUE,
    var score: Int = Int.MAX_VALUE
) {
    override fun toString(): String {
        return String.format("PathsToNodes[$source,$direction,$destination,$approach,$startScore,$score]");
    }
}
