package uk.j4numbers.adventofcode.advent_2024.day_16.utils

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.*
import kotlin.math.abs

class DijkstraGrapher constructor(private val graph: List<List<MapType>>) {
    private val logger = KotlinLogging.logger {}
    private val nodeCollection = mutableListOf<PathNode>();

    internal fun generatePathsToExplore(coordinateToTest: Coordinate): List<PathToNode> {
        val paths = mutableListOf<PathToNode>();
        if (graph[coordinateToTest.y][coordinateToTest.x + 1] != MapType.WALL) {
            paths.add(PathToNode(coordinateToTest, Direction.RIGHT));
        }
        if (graph[coordinateToTest.y][coordinateToTest.x - 1] != MapType.WALL) {
            paths.add(PathToNode(coordinateToTest, Direction.LEFT));
        }
        if (graph[coordinateToTest.y + 1][coordinateToTest.x] != MapType.WALL) {
            paths.add(PathToNode(coordinateToTest, Direction.DOWN));
        }
        if (graph[coordinateToTest.y - 1][coordinateToTest.x] != MapType.WALL) {
            paths.add(PathToNode(coordinateToTest, Direction.UP));
        }
        return paths;
    }

    init {
        for (yIdx in graph.indices) {
            for (xIdx in graph[yIdx].indices) {
                if (graph[yIdx][xIdx] == MapType.START) {
                    nodeCollection.add(
                        PathNode(
                            coordinate = Coordinate(x = xIdx, y = yIdx),
                            nodeType = NodeType.START_NODE,
                            approach = Direction.RIGHT,
                            paths = generatePathsToExplore(Coordinate(xIdx, yIdx)),
                            score = 0,
                        )
                    )
                }
                if (graph[yIdx][xIdx] == MapType.END) {
                    nodeCollection.add(
                        PathNode(
                            coordinate = Coordinate(x = xIdx, y = yIdx),
                            nodeType = NodeType.END_NODE,
                            paths = generatePathsToExplore(Coordinate(xIdx, yIdx)),
                        )
                    )
                }
                if (graph[yIdx][xIdx] == MapType.EMPTY_SPACE) {
                    val foundPaths = generatePathsToExplore(Coordinate(xIdx, yIdx))
                    if (foundPaths.size > 2) {
                        nodeCollection.add(
                            PathNode(
                                coordinate = Coordinate(x = xIdx, y = yIdx),
                                nodeType = NodeType.INTERMEDIATE_NODE,
                                paths = foundPaths,
                            )
                        )
                    } else if (foundPaths.size == 1) {
                        nodeCollection.add(
                            PathNode(
                                coordinate = Coordinate(x = xIdx, y = yIdx),
                                nodeType = NodeType.DEAD_END,
                                paths = foundPaths,
                            )
                        )
                    }
                }
            }
        }
    }

    internal fun findNextNode(): Int {
        var bestNode = -1;
        var bestNodeValue = Int.MAX_VALUE

        for (idx in nodeCollection.indices) {
            if (nodeCollection[idx].score in 0 until bestNodeValue
                && nodeCollection[idx].paths.any { path -> path.approach == Direction.UNKNOWN }
            ) {
                bestNode = idx;
                bestNodeValue = nodeCollection[idx].score;
            }
        }

        return bestNode;
    }

    internal fun translateDirectionToCoordinate(direction: Direction): Coordinate {
        return when (direction) {
            Direction.UP -> Coordinate(0, -1)
            Direction.DOWN -> Coordinate(0, 1)
            Direction.LEFT -> Coordinate(-1, 0)
            Direction.RIGHT -> Coordinate(1, 0)
            Direction.UNKNOWN -> Coordinate(-1, -1)
        }
    }

    internal fun translateCoordinateToDirection(coordinate: Coordinate): Direction {
        return when (coordinate) {
            Coordinate(1, 0) -> Direction.RIGHT
            Coordinate(-1, 0) -> Direction.LEFT
            Coordinate(0, 1) -> Direction.DOWN
            Coordinate(0, -1) -> Direction.UP
            else -> Direction.UNKNOWN
        }
    }

    internal fun calculateTurn(startDirection: Direction, targetDirection: Direction): Int {
        var diff = abs(startDirection.angle - targetDirection.angle);
        if (diff == 270) {
            diff = 90;
        }
        return (diff / 90) * 1000;
    }

    internal fun explorePath(startPosition: Coordinate, startScore: Int, pathToExplore: PathToNode, startDirection: Direction): Pair<Int, Int> {
        var turnScore = 0
        var length = 0;

        pathToExplore.startScore = calculateTurn(startDirection, pathToExplore.direction) + startScore;

        var foundNode = -1;
        var workingCoord = startPosition;
        var movement = pathToExplore.direction;

        while (foundNode < 0) {
            var trueMove = translateDirectionToCoordinate(movement);
            if (graph[workingCoord.y + trueMove.y][workingCoord.x + trueMove.x] == MapType.WALL) {
                val newDir = generatePathsToExplore(Coordinate(workingCoord.x, workingCoord.y))
                    .filter { ptn -> ptn.direction != translateCoordinateToDirection(Coordinate(trueMove.x * -1, trueMove.y * -1)) }[0].direction;
                turnScore += calculateTurn(movement, newDir)
                movement = newDir;
                trueMove = translateDirectionToCoordinate(movement);
            }

            logger.debug { "Moving from working coord $workingCoord on vector $trueMove (working score: ${turnScore * length})" }

            workingCoord = Coordinate(workingCoord.x + trueMove.x, workingCoord.y + trueMove.y);
            length += 1;

            for (idx in nodeCollection.indices) {
                if (nodeCollection[idx].coordinate == workingCoord) {
                    foundNode = idx;
                }
            }
        }

        pathToExplore.destination = workingCoord;
        pathToExplore.approach = movement;
        pathToExplore.length = length;
        pathToExplore.score = turnScore + length;

        return Pair(foundNode, turnScore + length);
    }

    internal fun dijkstraExplore(): List<PathNode> {
        var nextNode = findNextNode();
        logger.debug { "Running dijkstra exploration... First node is ${nodeCollection[nextNode]}" };

        while (nextNode > -1) {
            for (path in nodeCollection[nextNode].paths) {
                logger.debug { "Inspecting path $path of node ${nodeCollection[nextNode]}" };
                val (terminalIdx, score) = explorePath(nodeCollection[nextNode].coordinate, nodeCollection[nextNode].score, path, nodeCollection[nextNode].approach);

                logger.debug { "Found terminal node of ${nodeCollection[terminalIdx]} with an additive score of $score" };
                if (nodeCollection[terminalIdx].score > path.startScore + score) {
                    nodeCollection[terminalIdx].score = path.startScore + score;
                    nodeCollection[terminalIdx].approach = path.approach;
                }
            }

            nextNode = findNextNode();
        }

        return nodeCollection;
    }
}
