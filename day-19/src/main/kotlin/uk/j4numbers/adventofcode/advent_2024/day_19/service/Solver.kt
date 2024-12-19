package uk.j4numbers.adventofcode.advent_2024.day_19.service

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_19.pojo.PuzzleInput

class Solver constructor(val inputs: PuzzleInput) {
    private val logger = KotlinLogging.logger {}

    private val possiblePieces: MutableSet<String> = mutableSetOf()
    private val possibilityCache: MutableMap<String, Long> = mutableMapOf();

    init {
        possiblePieces.addAll(inputs.pieces);
    }

    private fun testPuzzlePossible(puzzle: String): Long {
        if (possibilityCache.containsKey(puzzle)) {
            logger.debug { "Cache hit! ${possibilityCache[puzzle]} possibilities for $puzzle" }
        } else {
            logger.debug { "Cache miss! Researching possibilities for $puzzle" }
            var possibilities = 0L;

            possiblePieces.forEach() { p ->
                if (puzzle.startsWith(p)) {
                    val updatedTest = puzzle.slice(p.length until puzzle.length);
                    logger.debug { "Found next matching pattern for '$puzzle' - '$p' - leaving '$updatedTest'" };
                    possibilities += if (updatedTest.isEmpty()) {
                        1;
                    } else {
                        testPuzzlePossible(updatedTest);
                    }
                }
            }

            logger.debug { "Cache save! Found $possibilities possibilities for $puzzle" }
            possibilityCache[puzzle] = possibilities;
        }

        return possibilityCache[puzzle]!!;
    }

    fun calcPossibleCombinations(): Pair<Int, Long> {
        var foundPossiblePuzzles = 0;
        var totalSolutionsFound = 0L;

        for (puzzle in inputs.puzzles) {
            logger.debug { "Testing puzzle '$puzzle' for solution" }
            val solutionsFound = testPuzzlePossible(puzzle);
            if (solutionsFound > 0) {
                logger.debug { "Found possible puzzle $puzzle!" };
                foundPossiblePuzzles += 1;
                totalSolutionsFound += solutionsFound;
            }
        }

        return Pair(foundPossiblePuzzles, totalSolutionsFound);
    }
}
