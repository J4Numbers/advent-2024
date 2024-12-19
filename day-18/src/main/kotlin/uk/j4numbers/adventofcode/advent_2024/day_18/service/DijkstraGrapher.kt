package uk.j4numbers.adventofcode.advent_2024.day_18.service

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_18.pojo.Coordinate
import uk.j4numbers.adventofcode.advent_2024.day_18.pojo.PathNode

class DijkstraGrapher constructor(private val obstacles: List<Coordinate>, private val axisLimit: Int) {
    private val logger = KotlinLogging.logger {}
    private val nodeCollection = mutableListOf<PathNode>();

    private fun coordinateIsValid(coordToTest: Coordinate): Boolean {
        return coordToTest.x in 0..axisLimit &&
                coordToTest.y in 0..axisLimit;
    }

    private fun generatePathsToExplore(coordinate: Coordinate): List<Coordinate> {
        val coordList = mutableListOf<Coordinate>();

        val up = Coordinate(coordinate.x, coordinate.y - 1);
        if (coordinateIsValid(up) && !obstacles.contains(up)) {
            coordList.add(up);
        }

        val down = Coordinate(coordinate.x, coordinate.y + 1);
        if (coordinateIsValid(down) && !obstacles.contains(down)) {
            coordList.add(down);
        }

        val left = Coordinate(coordinate.x - 1, coordinate.y);
        if (coordinateIsValid(left) && !obstacles.contains(left)) {
            coordList.add(left);
        }

        val right = Coordinate(coordinate.x + 1, coordinate.y);
        if (coordinateIsValid(right) && !obstacles.contains(right)) {
            coordList.add(right);
        }

        return coordList;
    }

    init {
        for (yIdx in 0..axisLimit) {
            for (xIdx in 0..axisLimit) {
                val curCoord = Coordinate(xIdx, yIdx);
                if (!obstacles.contains(curCoord)) {
                    nodeCollection.add(PathNode(
                        coordinate = curCoord,
                        neighbours = generatePathsToExplore(curCoord),
                    ));
                }
            }
        }
        nodeCollection.filter { n -> n.coordinate == Coordinate(0, 0) }[0].score = 0;
    }

    internal fun findNextNode(): Int {
        var bestNode = -1;
        var bestNodeValue = Int.MAX_VALUE

        for (idx in nodeCollection.indices) {
            if (nodeCollection[idx].score in 0 until bestNodeValue
                && !nodeCollection[idx].scanned
            ) {
                bestNode = idx;
                bestNodeValue = nodeCollection[idx].score;
            }
        }

        return bestNode;
    }

    internal fun findNodeAtCoord(coordToCheck: Coordinate): Int {
        var idx = -1;
        nodeCollection.forEachIndexed { testIdx, node ->
            if (node.coordinate == coordToCheck) {
                idx = testIdx;
            }
        }

        return idx;
    }

    internal fun dijkstraExplore(): List<PathNode> {
        var nextNode = findNextNode();
        logger.debug { "Running dijkstra exploration... First node is ${nodeCollection[nextNode]}" };

        while (nextNode > -1) {
            for (nxtCoord in nodeCollection[nextNode].neighbours) {
                val nxtIdx = findNodeAtCoord(nxtCoord);
                if (nxtIdx > -1) {
                    logger.debug { "Inspecting neighbour ${nodeCollection[nxtIdx]} of node ${nodeCollection[nextNode]}" };
                    val score = nodeCollection[nextNode].score + 1;

                    logger.debug { "Comparing new score to reach ${nodeCollection[nxtIdx].coordinate} of $score against previous score of ${nodeCollection[nxtIdx].score}" };
                    if (nodeCollection[nxtIdx].score > score) {
                        nodeCollection[nxtIdx].score = score;
                    }
                }
            }

            nodeCollection[nextNode].scanned = true;

            nextNode = findNextNode();
        }

        return nodeCollection;
    }

    fun printMap() {
        for (yIdx in 0..axisLimit) {
            var outLine = "";
            for (xIdx in 0..axisLimit) {
                outLine += if (obstacles.contains(Coordinate(xIdx, yIdx))) {
                    "#"
                } else {
                    "."
                }
            }
            logger.debug { outLine };
        }
    }
}
