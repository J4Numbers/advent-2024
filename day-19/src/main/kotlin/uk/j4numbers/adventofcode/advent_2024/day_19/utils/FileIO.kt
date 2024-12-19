package uk.j4numbers.adventofcode.advent_2024.day_19.utils

import java.io.File
import java.io.InputStream

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_19.pojo.PuzzleInput

class FileIO {
    private val logger = KotlinLogging.logger {}

    fun readFile(inputFile: String): PuzzleInput {
        logger.debug("Reading $inputFile...");
        val inputStream: InputStream = File(inputFile).inputStream()

        val pieces = mutableListOf<String>();
        val puzzles = mutableListOf<String>();

        val pieceRegex = Regex("[wubrg]+")

        inputStream.bufferedReader().forEachLine { line ->
            if (pieces.isEmpty()) {
                pieces.addAll(
                    pieceRegex
                        .findAll(line)
                        .map { it.value }
                        .toList()
                );
            } else {
                val piecePattern = pieceRegex.matchEntire(line);
                if (piecePattern != null) {
                    puzzles.add(piecePattern.value);
                }
            }
        }

        return PuzzleInput(pieces, puzzles);
    }
}
