package uk.j4numbers.adventofcode.advent_2024.day_17

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_17.service.Program
import uk.j4numbers.adventofcode.advent_2024.day_17.utils.FileIO
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

    val computer = Program(fileContents);
    val outputs = computer.executeProgram();

    logger.info { "The following was returned from the program: '${outputs.joinToString(separator = ",")}'" };

    val reversedReg = computer.reverseFromInstructions();
    logger.info { "After attempting a reverse instruction register: $reversedReg" };
}
