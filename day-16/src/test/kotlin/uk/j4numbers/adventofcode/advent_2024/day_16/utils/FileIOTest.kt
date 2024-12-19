package uk.j4numbers.adventofcode.advent_2024.day_16.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equals.shouldBeEqual
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.MapType
import uk.j4numbers.adventofcode.advent_2024.day_16.utils.FileIO

class FileIOTest: FunSpec ({
    test("Reading a file should return its contents") {
        val fileReader = FileIO();
        val fileContents = fileReader.readFile("src/test/resources/test-file.txt");

        fileContents shouldHaveSize 3;
        fileContents[1] shouldBeEqual listOf(MapType.WALL, MapType.START, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.END, MapType.WALL);
    }
})
