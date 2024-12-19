package uk.j4numbers.adventofcode.advent_2024.day_19

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_19.service.Solver
import uk.j4numbers.adventofcode.advent_2024.day_19.utils.FileIO
import java.io.File

val logger = KotlinLogging.logger {}

fun testArgs(args: Array<String>) {
    require(args.isNotEmpty()) { "Number of arguments must be at least 1." }
    require(File(args[0]).exists()) { "Please provide a file that exists." }
}

fun main(args: Array<String>) {
    testArgs(args);

    val fileParser = FileIO();

    val fileContents = fileParser.readFile(args[0]);

    logger.info { fileContents }

    val solver = Solver(fileContents);
    val possiblePuzzles = solver.calcPossibleCombinations();

    logger.info { "There were ${possiblePuzzles.first} possible combinations out of ${fileContents.puzzles.size}" };
    logger.info { "There were ${possiblePuzzles.second} different combinations for the above possible puzzles" };
}
