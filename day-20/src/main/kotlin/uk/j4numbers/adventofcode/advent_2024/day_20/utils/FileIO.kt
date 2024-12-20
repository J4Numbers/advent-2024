package uk.j4numbers.adventofcode.advent_2024.day_20.utils

import java.io.File
import java.io.InputStream

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_20.pojo.Coordinate
import uk.j4numbers.adventofcode.advent_2024.day_20.pojo.Node
import uk.j4numbers.adventofcode.advent_2024.day_20.pojo.NodeType

class FileIO {
    private val logger = KotlinLogging.logger {}

    fun readFile(inputFile: String): List<Node> {
        logger.debug("Reading $inputFile...");
        val inputStream: InputStream = File(inputFile).inputStream()

        val nodes = mutableListOf<Node>();
        val nodeRegex = Regex("[.SE]");

        var lineNumber = 0;

        inputStream.bufferedReader().forEachLine { line ->
            nodes.addAll(
                nodeRegex
                    .findAll(line)
                    .map { n ->
                        val set = n.groups[0]!!;
                        val score = if (set.value == "S") { 0 } else { Int.MAX_VALUE };
                        val type = if (set.value == ".") { NodeType.INTERMEDIARY } else if (set.value == "S") { NodeType.START } else { NodeType.END };

                        Node(
                            coordinate = Coordinate(set.range.first, lineNumber),
                            type = type,
                            score = score,
                        )
                    }
                    .toList()
            );

            lineNumber += 1;
        }

        return nodes;
    }
}
