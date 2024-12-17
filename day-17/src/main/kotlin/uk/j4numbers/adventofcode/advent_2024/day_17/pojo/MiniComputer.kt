package uk.j4numbers.adventofcode.advent_2024.day_17.pojo

class MiniComputer private constructor(
    val regA: Long = 0L,
    val regB: Long = 0L,
    val regC: Long = 0L,
    val instructions: List<Int> = emptyList()
) {
    data class Builder(
        var regA: Long = 0,
        var regB: Long = 0,
        var regC: Long = 0,
        var instructions: MutableList<Int> = mutableListOf(),
    ) {
        fun regA(regA: Long) = apply { this.regA = regA }
        fun regB(regB: Long) = apply { this.regB = regB }
        fun regC(regC: Long) = apply { this.regC = regC }
        fun instructions(instructions: MutableList<Int>) = apply { this.instructions = instructions }
        fun build() = MiniComputer(regA, regB, regC, instructions);
    }
}
