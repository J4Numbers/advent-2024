package uk.j4numbers.adventofcode.advent_2024.day_20

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_20.service.Solver
import uk.j4numbers.adventofcode.advent_2024.day_20.utils.FileIO
import java.io.File

val logger = KotlinLogging.logger {}

fun testArgs(args: Array<String>) {
    require(args.size > 2) { "Number of arguments must be at least 3." }
    require(File(args[0]).exists()) { "Please provide a file that exists." }
    require(args[1].toIntOrNull() != null) { "please require a number for the requested improvement score" }
    require(args[2].toIntOrNull() != null) { "please require a number for the number of allowed cheat steps" }
}

fun main(args: Array<String>) {
    testArgs(args);

    val improvementRequired = args[1].toInt();
    val skipSteps = args[2].toInt();

    val fileParser = FileIO();

    val fileContents = fileParser.readFile(args[0]);
    Solver.associateAllNeighbours(fileContents);

    val solver = Solver(fileContents);
    val cheats = solver.longCheat(improvementRequired, skipSteps);

    logger.info { "Found $cheats possible cheats for $skipSteps skipped steps which improved at least $improvementRequired steps" };
}
