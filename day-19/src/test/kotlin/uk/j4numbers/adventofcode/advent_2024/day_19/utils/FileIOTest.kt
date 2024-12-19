package uk.j4numbers.adventofcode.advent_2024.day_19.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldHaveSize

class FileIOTest: FunSpec ({
    test("Reading a file should return its contents") {
        val fileReader = FileIO();
        val fileContents = fileReader.readFile("src/test/resources/test-file.txt");

        fileContents.pieces shouldHaveSize 4;
        fileContents.puzzles shouldHaveSize 3;
    }
})