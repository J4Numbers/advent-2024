package uk.j4numbers.adventofcode.advent_2024.day_18.utils

import java.io.File
import java.io.InputStream

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_18.pojo.Coordinate

class FileIO {
    private val logger = KotlinLogging.logger {}

    fun readFile(inputFile: String, limit: Int): List<Coordinate> {
        logger.debug("Reading $inputFile...");
        val inputStream: InputStream = File(inputFile).inputStream()
        val coordList = mutableListOf<Coordinate>()
        var lineCount = 0;

        inputStream.bufferedReader().forEachLine { line ->
            val coordPattern = Regex("^([0-9]+),([0-9]+)$").matchEntire(line);
            if (coordPattern != null && lineCount < limit) {
                logger.debug { "Found new coordinate block - ${coordPattern.groupValues[0]}" };
                coordList.add(Coordinate(coordPattern.groupValues[1].toInt(), coordPattern.groupValues[2].toInt()));
            }
            lineCount += 1;
        }

        return coordList;
    }
}
