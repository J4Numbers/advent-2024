package uk.j4numbers.adventofcode.advent_2024.day_16.pojo

enum class Direction constructor(val angle: Int) {
    UP(0),
    RIGHT(90),
    DOWN(180),
    LEFT(270),
    UNKNOWN(-1),
}
