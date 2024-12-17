package uk.j4numbers.adventofcode.advent_2024.day_16.utils

import mu.KotlinLogging
import uk.j4numbers.adventofcode.advent_2024.day_16.pojo.*

val logger = KotlinLogging.logger {}

fun backtrackCount(nodeCollection: List<PathNode>): Int {
    val targetNodes = mutableListOf<PathNode>(nodeCollection.filter { it.nodeType == NodeType.END_NODE }[0]);
    val visitedNodes = mutableListOf<PathNode>();
    val collectedPaths = mutableSetOf<PathToNode>();

    while (targetNodes.isNotEmpty()) {
        val nodeToCheck = targetNodes.removeFirst();
        visitedNodes.add(nodeToCheck);

        for (nodeIdx in nodeCollection.indices) {
            nodeCollection[nodeIdx].paths
                .filter { pathToNode -> pathToNode.destination == nodeToCheck.coordinate }
                .filter { pathToNode ->
                    logger.debug { "Introspecting path $pathToNode from ${nodeCollection[nodeIdx]} against current target $nodeToCheck" };
                    nodeToCheck.score - pathToNode.score == pathToNode.startScore
                            || collectedPaths.any {pathToTest -> pathToTest.source == nodeToCheck.coordinate && pathToTest.startScore - pathToNode.score == pathToNode.startScore}
                }
                .forEach { pathToNode ->
                    logger.debug { "Found new valid optimal backtrack from $nodeToCheck (${nodeCollection[nodeIdx]} on ${pathToNode})" };
                    collectedPaths.add(pathToNode);
                    targetNodes.add(nodeCollection[nodeIdx]);
                };
        }
    }

    val basicVisits = collectedPaths.map { it.length }.reduce { acc, i -> acc + i } + 1;
    val distinctPoints = collectedPaths.distinctBy { path -> path.destination }.size;
    return basicVisits - (collectedPaths.size - distinctPoints);
}
