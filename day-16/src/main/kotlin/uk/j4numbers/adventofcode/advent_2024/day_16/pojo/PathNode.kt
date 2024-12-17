package uk.j4numbers.adventofcode.advent_2024.day_16.pojo

class PathNode constructor(
    val coordinate: Coordinate,
    val nodeType: NodeType,
    val paths: List<PathToNode> = emptyList(),
    var approach: Direction = Direction.UNKNOWN,
    var score: Int = Int.MAX_VALUE,
) {
    override fun toString(): String {
        return String.format("PathNode[$coordinate,$nodeType,$approach,$score]");
    }
}
