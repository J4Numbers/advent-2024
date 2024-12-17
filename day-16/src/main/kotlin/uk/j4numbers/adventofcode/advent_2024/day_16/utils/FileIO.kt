package uk.j4numbers.adventofcode.advent_2024.day_16.utils

import java.io.File
import java.io.InputStream

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.MapType

class FileIO {
    private val logger = KotlinLogging.logger {}

    fun readFile(inputFile: String): List<List<MapType>> {
        logger.debug("Reading $inputFile...");
        val inputStream: InputStream = File(inputFile).inputStream()
        val lineList = mutableListOf<List<MapType>>()

        inputStream.bufferedReader().forEachLine { line ->
            if (line.matches(Regex("^[.#SE]+$"))) {
                lineList.add(line.chunked(1).map { char ->
                    var valType = MapType.WALL;
                    when (char) {
                        "." -> valType = MapType.EMPTY_SPACE
                        "#" -> valType = MapType.WALL
                        "S" -> valType = MapType.START
                        "E" -> valType = MapType.END
                    }
                    valType;
                });
            }
        }

        return lineList;
    }
}
