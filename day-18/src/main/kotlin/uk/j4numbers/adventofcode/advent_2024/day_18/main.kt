package uk.j4numbers.adventofcode.advent_2024.day_18

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_18.pojo.Coordinate
import uk.j4numbers.adventofcode.advent_2024.day_18.service.DijkstraGrapher
import uk.j4numbers.adventofcode.advent_2024.day_18.utils.FileIO
import java.io.File

val logger = KotlinLogging.logger {}

fun testArgs(args: Array<String>) {
    require(args.size > 2) { "Number of arguments must be at least 3." }
    require(File(args[0]).exists()) { "Please provide a file that exists." }
    require(args[1].toIntOrNull() != null) { "Please provide a valid integer value for the size of the grid" }
    require(args[2].toIntOrNull() != null) { "Please provide a valid integer value for the number of lines to read in" }
}

fun main(args: Array<String>) {
    testArgs(args);

    val fileParser = FileIO();

    val axisLimit = args[1].toInt();
    val readLimit = args[2].toInt();

    val fileContents = fileParser.readFile(args[0], readLimit);

    val grapher = DijkstraGrapher(fileContents, axisLimit);

    val outputs = grapher.dijkstraExplore();
    val lastNode = outputs.filter { n -> n.coordinate == Coordinate(axisLimit, axisLimit) }[0]
    logger.info { "The following was returned from the program: '${lastNode.score}'" };

    grapher.printMap();
}
