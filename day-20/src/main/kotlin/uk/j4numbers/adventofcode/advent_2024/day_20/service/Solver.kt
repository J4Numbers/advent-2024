package uk.j4numbers.adventofcode.advent_2024.day_20.service

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_20.pojo.Coordinate
import uk.j4numbers.adventofcode.advent_2024.day_20.pojo.Node
import kotlin.math.abs

class Solver(private val nodeList: List<Node>) {
    private val logger = KotlinLogging.logger {}

    companion object {
        private val logger = KotlinLogging.logger {}

        fun associateAllNeighbours(nodeList: List<Node>) {
            for (node in nodeList) {
                logger.debug { "Associating neighbours of node at coordinate ${node.coordinate}" }
                val upCoord = Coordinate(node.coordinate.x, node.coordinate.y - 1);
                val skipUpCoord = Coordinate(node.coordinate.x, node.coordinate.y - 2);
                if (nodeList.any { n -> n.coordinate == upCoord }) {
                    logger.debug { "Found neighbour above node ${node.coordinate} - $upCoord" }
                    node.addNeighbour(Pair(1, upCoord));
                } else if (nodeList.any { n -> n.coordinate == skipUpCoord }) {
                    logger.debug { "Found cheat above node ${node.coordinate} - $skipUpCoord" }
                    node.addNeighbour(Pair(2, skipUpCoord))
                }

                val downCoord = Coordinate(node.coordinate.x, node.coordinate.y + 1);
                val skipDownCoord = Coordinate(node.coordinate.x, node.coordinate.y + 2);
                if (nodeList.any { n -> n.coordinate == downCoord }) {
                    logger.debug { "Found neighbour below node ${node.coordinate} - $downCoord" }
                    node.addNeighbour(Pair(1, downCoord));
                } else if (nodeList.any { n -> n.coordinate == skipDownCoord }) {
                    logger.debug { "Found cheat below node ${node.coordinate} - $skipDownCoord" }
                    node.addNeighbour(Pair(2, skipDownCoord))
                }

                val leftCoord = Coordinate(node.coordinate.x - 1, node.coordinate.y);
                val skipLeftCoord = Coordinate(node.coordinate.x - 2, node.coordinate.y);
                if (nodeList.any { n -> n.coordinate == leftCoord }) {
                    logger.debug { "Found neighbour left of node ${node.coordinate} - $leftCoord" }
                    node.addNeighbour(Pair(1, leftCoord));
                } else if (nodeList.any { n -> n.coordinate == skipLeftCoord }) {
                    logger.debug { "Found cheat left of node ${node.coordinate} - $skipLeftCoord" }
                    node.addNeighbour(Pair(2, skipLeftCoord))
                }

                val rightCoord = Coordinate(node.coordinate.x + 1, node.coordinate.y);
                val skipRightCoord = Coordinate(node.coordinate.x + 2, node.coordinate.y);
                if (nodeList.any { n -> n.coordinate == rightCoord }) {
                    logger.debug { "Found neighbour right of node ${node.coordinate} - $rightCoord" }
                    node.addNeighbour(Pair(1, rightCoord));
                } else if (nodeList.any { n -> n.coordinate == skipRightCoord }) {
                    logger.debug { "Found cheat right of node ${node.coordinate} - $skipRightCoord" }
                    node.addNeighbour(Pair(2, skipRightCoord))
                }
            }
        }
    }

    private fun findNextNode(): Int {
        var nodeIdx = -1;
        var nodeScoreToBeat = Int.MAX_VALUE;

        for (idx in nodeList.indices) {
            if (
                !nodeList[idx].scanned
                && nodeScoreToBeat > nodeList[idx].score
            ) {
                nodeIdx = idx;
                nodeScoreToBeat = nodeList[idx].score;
            }
        }

        return nodeIdx;
    }

    init {
        var nodeToInspect = findNextNode();
        val step = 1;
        logger.debug { "Running initial scoring algorithm over all nodes" };

        while (nodeToInspect > -1) {
            for (neighbour in nodeList[nodeToInspect].getNeighbours().filter { it.first == step }) {
                val neighbourNode = nodeList.filter { n -> n.coordinate == neighbour.second }[0];
                if (neighbourNode.score > nodeList[nodeToInspect].score + step) {
                    neighbourNode.score = nodeList[nodeToInspect].score + step;
                    logger.debug { "Found new best score for node ${neighbourNode.coordinate} - ${neighbourNode.score}" };
                }
            }
            nodeList[nodeToInspect].scanned = true;

            nodeToInspect = findNextNode();
        }

        nodeList.forEach { n -> n.scanned = false };
    }

    private fun findNearNeighbourNodesBeatingSaving(startNode: Node, savingToBeat: Int, maxCheatSteps: Int): Int {
        var nodeCount = 0;

        for (xDiff in -maxCheatSteps..maxCheatSteps) {
            nodeCount += nodeList.filter { n ->
                n.coordinate.x == startNode.coordinate.x + xDiff
                        && n.coordinate.y in (-maxCheatSteps+abs(xDiff)+startNode.coordinate.y)..(maxCheatSteps-abs(xDiff)+startNode.coordinate.y)
                        && n.score - (abs(xDiff) + abs(startNode.coordinate.y-n.coordinate.y)) - startNode.score >= savingToBeat
            }.size
        }

        return nodeCount;
    }

    fun longCheat(savingToBeat: Int, maxCheatSteps: Int): Int {
        var discoveredBeatingCheats = 0;

        var nIdx = findNextNode();

        while (nIdx > -1) {
            val newCheats =
                findNearNeighbourNodesBeatingSaving(nodeList[nIdx], savingToBeat, maxCheatSteps)
            discoveredBeatingCheats += newCheats;
            if (newCheats > 0) {
                logger.debug { "Found $newCheats new valid cheats saving at least $savingToBeat steps from node ${nodeList[nIdx].coordinate} to a new total of $discoveredBeatingCheats" }
            }

            nodeList[nIdx].scanned = true;
            nIdx = findNextNode();
        }

        // Calculate desired value of node that would beat the saving
        // Find all nodes within 20 steps of the current node that would match or beat that value
        // Add that count to the total number of found cheats over time
        return discoveredBeatingCheats;
    }
}
