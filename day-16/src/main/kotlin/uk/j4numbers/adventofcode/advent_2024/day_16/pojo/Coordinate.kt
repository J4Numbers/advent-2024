package uk.j4numbers.adventofcode.advent_2024.day_16.pojo

class Coordinate constructor(val x: Int, val y: Int) {
    override fun toString(): String {
        return String.format("Coordinate[$x,$y]");
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Coordinate

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }
}
