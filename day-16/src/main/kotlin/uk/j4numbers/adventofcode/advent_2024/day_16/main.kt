package uk.j4numbers.adventofcode.advent_2024.day_16

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.NodeType
import uk.j4numbers.adventofcode.advent_2024.day_16.utils.FileIO
import uk.j4numbers.adventofcode.advent_2024.day_16.utils.DijkstraGrapher
import uk.j4numbers.adventofcode.advent_2024.day_16.utils.backtrackCount
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
    println(fileContents);

    val graph = DijkstraGrapher(fileContents);

    val exploredGraph = graph.dijkstraExplore();
    logger.info { "End node has a final score of ${exploredGraph.filter { it.nodeType == NodeType.END_NODE }[0].score}" };

    logger.info { "The backtrack count of the graph was ${backtrackCount(exploredGraph)}" };
}
