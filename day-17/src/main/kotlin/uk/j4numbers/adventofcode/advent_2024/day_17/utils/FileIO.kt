package uk.j4numbers.adventofcode.advent_2024.day_17.utils

import java.io.File
import java.io.InputStream

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_17.pojo.MiniComputer

class FileIO {
    private val logger = KotlinLogging.logger {}

    fun readFile(inputFile: String): MiniComputer {
        logger.debug("Reading $inputFile...");
        val inputStream: InputStream = File(inputFile).inputStream()
        val miniComputer = MiniComputer.Builder();

        inputStream.bufferedReader().forEachLine { line ->
            val regAPattern = Regex("^Register A: ([0-9]+)$").matchEntire(line);
            if (regAPattern != null) {
                logger.debug { "Found new value for register A - ${regAPattern.groupValues[1]}" };
                miniComputer.regA(regAPattern.groupValues[1].toLong());
            }
            val regBPattern = Regex("^Register B: ([0-9]+)$").matchEntire(line);
            if (regBPattern != null) {
                logger.debug { "Found new value for register B - ${regBPattern.groupValues[1]}" };
                miniComputer.regB(regBPattern.groupValues[1].toLong());
            }
            val regCPattern = Regex("^Register C: ([0-9]+)$").matchEntire(line);
            if (regCPattern != null) {
                logger.debug { "Found new value for register C - ${regCPattern.groupValues[1]}" };
                miniComputer.regC(regCPattern.groupValues[1].toLong());
            }

            if (line.startsWith("Program:", true)) {
                val instructions = Regex("([0-7])")
                    .findAll(line)
                    .map { it.value.toInt(8) }
                    .toMutableList();
                logger.debug { "Found new program instructions - $instructions" };
                miniComputer.instructions(instructions)
            }
        }

        return miniComputer.build();
    }
}
