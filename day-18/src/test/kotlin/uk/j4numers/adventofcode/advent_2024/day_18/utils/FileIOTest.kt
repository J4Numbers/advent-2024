package uk.j4numers.adventofcode.advent_2024.day_18.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import uk.j4numbers.adventofcode.advent_2024.day_18.utils.FileIO

class FileIOTest: FunSpec ({
    test("Reading a file should return its contents") {
        val fileReader = FileIO();
        val fileContents = fileReader.readFile("src/test/resources/test-file.txt", 5);

        fileContents shouldHaveSize 4;
    }
})
