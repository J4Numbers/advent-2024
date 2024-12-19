package uk.j4numbers.adventofcode.advent_2024.day_16.utils

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.Coordinate
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.Direction
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.MapType
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.PathToNode
import uk.j4numbers.adventofcode.advent_2024.day_16.utils.DijkstraGrapher

class DijkstraGrapherTest: FunSpec ({
    test("Get paths should return 1 path at a dead end") {
        val graphClass = DijkstraGrapher(listOf(
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
            listOf(MapType.WALL, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.WALL),
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
        ));

        val foundPaths = graphClass.generatePathsToExplore(Coordinate(1,1))

        foundPaths.size shouldBe 1;
    }

    test("Get paths should return 2 paths at a corner") {
        val graphClass = DijkstraGrapher(listOf(
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
            listOf(MapType.WALL, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.WALL),
            listOf(MapType.WALL, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.WALL),
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
        ));

        val foundPaths = graphClass.generatePathsToExplore(Coordinate(1,1))

        foundPaths.size shouldBe 2;
    }

    test("Get paths should return 2 paths in a straight line") {
        val graphClass = DijkstraGrapher(listOf(
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
            listOf(MapType.WALL, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.WALL),
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
        ));

        val foundPaths = graphClass.generatePathsToExplore(Coordinate(2,1))

        foundPaths.size shouldBe 2;
    }

    test("Get paths should return 3 paths in a T intersection") {
        val graphClass = DijkstraGrapher(listOf(
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
            listOf(MapType.WALL, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.WALL),
            listOf(MapType.WALL, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.WALL),
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
        ));

        val foundPaths = graphClass.generatePathsToExplore(Coordinate(2,1))

        foundPaths.size shouldBe 3;
    }

    test("Get paths should return 4 paths in a X intersection") {
        val graphClass = DijkstraGrapher(listOf(
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
            listOf(MapType.WALL, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.WALL),
            listOf(MapType.WALL, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.WALL),
            listOf(MapType.WALL, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.EMPTY_SPACE, MapType.WALL),
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
        ));

        val foundPaths = graphClass.generatePathsToExplore(Coordinate(2,2))

        foundPaths.size shouldBe 4;
    }

    test("Get next node on a new graph should return the start node") {
        val graphClass = DijkstraGrapher(listOf(
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
            listOf(MapType.WALL, MapType.START, MapType.EMPTY_SPACE, MapType.END, MapType.WALL),
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
        ));

        graphClass.findNextNode() shouldBe 0;
    }

    test("Test follow path on a new graph should calculate until the next node") {
        val graphClass = DijkstraGrapher(listOf(
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
            listOf(MapType.WALL, MapType.START, MapType.EMPTY_SPACE, MapType.END, MapType.WALL),
            listOf(MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL, MapType.WALL),
        ));

        val pathToExplore = PathToNode(Coordinate(1,1), Direction.RIGHT);
        val (idx, score) = graphClass.explorePath(Coordinate(1,1), 0, pathToExplore, Direction.RIGHT);

        idx shouldBe 1;
        score shouldBe 2;
    }

    test("Translating directions should represent their correct vectors") {
        val graphClass = DijkstraGrapher(emptyList());

        graphClass.translateDirectionToCoordinate(Direction.UP) shouldBe Coordinate(0, -1)
        graphClass.translateDirectionToCoordinate(Direction.DOWN) shouldBe Coordinate(0, 1)
        graphClass.translateDirectionToCoordinate(Direction.LEFT) shouldBe Coordinate(-1, 0)
        graphClass.translateDirectionToCoordinate(Direction.RIGHT) shouldBe Coordinate(1, 0)
        graphClass.translateDirectionToCoordinate(Direction.UNKNOWN) shouldBe Coordinate(-1, -1)
    }

    test("Translating coordinate vectors should represent their directions") {
        val graphClass = DijkstraGrapher(emptyList());

        graphClass.translateCoordinateToDirection(Coordinate(1, 0)) shouldBe Direction.RIGHT;
        graphClass.translateCoordinateToDirection(Coordinate(-1, 0)) shouldBe Direction.LEFT;
        graphClass.translateCoordinateToDirection(Coordinate(0, 1)) shouldBe Direction.DOWN;
        graphClass.translateCoordinateToDirection(Coordinate(0, -1)) shouldBe Direction.UP;
        graphClass.translateCoordinateToDirection(Coordinate(-1, -1)) shouldBe Direction.UNKNOWN;
    }

    test("Direction calculation should be 0 with the same direction") {
        val graphClass = DijkstraGrapher(emptyList());
        graphClass.calculateTurn(Direction.UP, Direction.UP) shouldBe 0;
    }

    test("Direction calculation should be 1000 with single changes of direction") {
        val graphClass = DijkstraGrapher(emptyList());
        graphClass.calculateTurn(Direction.UP, Direction.RIGHT) shouldBe 1000;
        graphClass.calculateTurn(Direction.UP, Direction.LEFT) shouldBe 1000;
        graphClass.calculateTurn(Direction.DOWN, Direction.RIGHT) shouldBe 1000;
        graphClass.calculateTurn(Direction.DOWN, Direction.LEFT) shouldBe 1000;
    }

    test("Direction calculation should be 2000 with double changes of direction") {
        val graphClass = DijkstraGrapher(emptyList());
        graphClass.calculateTurn(Direction.UP, Direction.DOWN) shouldBe 2000;
        graphClass.calculateTurn(Direction.LEFT, Direction.RIGHT) shouldBe 2000;
    }
})
