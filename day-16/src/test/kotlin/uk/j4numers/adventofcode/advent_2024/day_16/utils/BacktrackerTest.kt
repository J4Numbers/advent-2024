package uk.j4numers.adventofcode.advent_2024.day_16.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.*
import uk.j4numbers.adventofcode.advent_2024.day_16.utils.backtrackCount

class BacktrackerTest: FunSpec ({

    // Describes: #####
    //            #S.E#
    //            #####
    test("Basic spec returns accurate backtrack count") {
        val nodeColl = listOf(
            PathNode(Coordinate(1, 1), NodeType.START_NODE, listOf(PathToNode(Coordinate(1,1), Direction.RIGHT, Coordinate(3,1), Direction.RIGHT, 2, 0, 2)), Direction.RIGHT, 0),
            PathNode(Coordinate(3, 1), NodeType.END_NODE, listOf(PathToNode(Coordinate(3, 1), Direction.LEFT, Coordinate(1, 1), Direction.LEFT, 2, 2000, 2)), Direction.RIGHT, 2),
        );

        backtrackCount(nodeColl) shouldBe 3;
    }

    // Describes: #########
    //            ####E####
    //            #.......#
    //            #.#####.#
    //            #.......#
    //            ####S####
    //            #########
    test("Split spec returns accurate tracks") {
        val nodeColl = listOf(
            PathNode(Coordinate(4, 5), NodeType.START_NODE, listOf(PathToNode(Coordinate(4, 5), Direction.UP, Coordinate(4, 4), Direction.UP, 1, 1000, 1)), Direction.RIGHT, 0),
            PathNode(Coordinate(4, 4), NodeType.INTERMEDIATE_NODE, listOf(
                PathToNode(Coordinate(4, 4), Direction.LEFT, Coordinate(4, 2), Direction.RIGHT, 8, 2001, 2008),
                PathToNode(Coordinate(4, 4), Direction.RIGHT, Coordinate(4, 2), Direction.LEFT, 8, 2001, 2008),
                PathToNode(Coordinate(4, 4), Direction.DOWN, Coordinate(4, 5), Direction.DOWN, 1, 3001, 1),
            ), Direction.UP, 1001),
            PathNode(Coordinate(4, 2), NodeType.INTERMEDIATE_NODE, listOf(
                PathToNode(Coordinate(4, 2), Direction.UP, Coordinate(4, 1), Direction.UP, 1, 5009, 1),
                PathToNode(Coordinate(4, 2), Direction.LEFT, Coordinate(4, 4), Direction.RIGHT, 8, 4009, 2008),
                PathToNode(Coordinate(4, 2), Direction.RIGHT, Coordinate(4, 4), Direction.LEFT, 8, 4009, 2008),
            ), Direction.RIGHT, 4009),
            PathNode(Coordinate(4, 1), NodeType.END_NODE, listOf(PathToNode(Coordinate(4,1), Direction.DOWN, Coordinate(4,2), Direction.DOWN, 1, 7010, 1)), Direction.UP, 5010),
        );

        backtrackCount(nodeColl) shouldBe 18;
    }

    // Describes: #####
    //            #..E#
    //            #.#.#
    //            #S..#
    //            #####
    test("Direct split spec returns only the least score backtrack") {
        val nodeColl = listOf(
            PathNode(Coordinate(1, 3), NodeType.START_NODE, listOf(
                PathToNode(Coordinate(1,3), Direction.RIGHT, Coordinate(3,1), Direction.UP, 4, 0, 1004),
                PathToNode(Coordinate(1,3), Direction.UP, Coordinate(3,1), Direction.RIGHT, 4, 1000, 1004),
            ), Direction.RIGHT, 0),
            PathNode(Coordinate(3, 1), NodeType.END_NODE, listOf(
                PathToNode(Coordinate(3, 1), Direction.LEFT, Coordinate(1, 3), Direction.DOWN, 4, 2004,  1004),
                PathToNode(Coordinate(3, 1), Direction.DOWN, Coordinate(1, 3), Direction.LEFT, 4, 3004, 1004),
            ), Direction.UP, 1004),
        );

        backtrackCount(nodeColl) shouldBe 5;
    }
})
