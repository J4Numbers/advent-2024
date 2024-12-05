#!/usr/bin/env node

import yargs from 'yargs';
import { hideBin } from 'yargs/helpers';

import * as fs from 'fs';

const argsEngine = yargs(hideBin(process.argv));
const args = argsEngine.wrap(argsEngine.terminalWidth())
  .env('J4_ADVENT_04')
  .options({
    input: {
      alias: 'i',
      type: 'string',
      description: 'The input file to run this algorithm on',
      demandOption: true,
    },
    mode: {
      options: ['XMAS', 'X'],
      default: 'XMAS',
      description: 'Change how to run the wordsearch. X will search for intersecting MAS lines.',
    },
    debug: {
      type: 'boolean',
      default: false,
      description: 'Enable debug logging',
    },
  })
  .help()
  .parse();

const debug = args.debug;

// Base variables
const lineHistory = [];
const openPaths = [];

/**
 * @param line - Print this line if console debug is enabled
 */
const debugLine = (line) => {
  if (debug) {
    console.log(line);
  }
};

/**
 * Generate a potential XMAS path from a starting location and when given a
 * vector to investigate.
 *
 * @param {{index: number, line: number}} start - The starting location.
 * @param {{index: number, line: number}} vector - The vector to apply to the
 * starting location to build up a path.
 * @returns {Array<{index: number, line: number}>} - A built-up path which
 * might contain a valid 'XMAS' string.
 */
const generatePath = (start, vector) => {
  let path = [start];
  for (let i=1; i<4; ++i) {
    path.push({
      line: start.line + (vector.line * i),
      index: start.index + (vector.index * i),
    });
  }
  return path;
};

/**
 * Given a list of potential paths, find which paths are actually valid when
 * we know the dimensions of our grid.
 *
 * @param {Array<Array<{index: number, line: number}>>} paths - Array of paths
 * which each contain steps to make a word.
 * @param {number} maxWidth - The maximum width (or index) that our grid is
 * made of.
 * @param {number} maxLength - The maximum length (or lines) that our grid is
 * made of.
 * @returns {Array<{index: number, line: number}>} - The list of valid paths
 * that are all completely within our grid.
 */
const calculateValidPaths = (paths, maxWidth, maxLength) => {
  return paths
    .filter((path) =>
        path.filter((step) =>
            (step.index < 0 || step.index >= maxWidth) || // Must not go past width boundaries
            (step.line < 0 || step.line >= maxLength)     // Must not go past height boundaries
        ).length === 0
    );
}

/**
 * When given a starting location, explode out the many different options from
 * which XMAS could be formed, and only return the paths which are valid within
 * our grid.
 *
 * @param {{index: number, line: number}} start - The starting co-ordinate
 * within our grid.
 * @param {number} maxWidth - The maximum width (or index) that our grid is
 * made of.
 * @param {number} maxLength - The maximum length (or lines) that our grid is
 * made of.
 * @returns {Array<{index: number, line: number}>} - A list of valid paths
 * that are within our grid.
 */
const generateXmasSearches = (start, maxWidth, maxLength) => {
  let searchPaths = [
      generatePath(start, {index: -1, line: -1}), // Up left
      generatePath(start, {index:  0, line: -1}), // Up
      generatePath(start, {index:  1, line: -1}), // Up right
      generatePath(start, {index: -1, line:  0}), // Back
      generatePath(start, {index:  1, line:  0}), // Forwards
      generatePath(start, {index: -1, line:  1}), // Down left
      generatePath(start, {index:  0, line:  1}), // Down
      generatePath(start, {index:  1, line:  1}), // Down right
  ];
  const validPaths = calculateValidPaths(searchPaths, maxWidth, maxLength);
  debugLine(`Found ${validPaths.length} valid XMAS paths from start point [${start.index},${start.line}]`);
  return validPaths;
};

/**
 * When given a starting location, explode out the different options from
 * which MAS could be formed in the shape of an X. Only return paths which
 * are within our grid and are therefore valid.
 *
 * @param {{index: number, line: number}} start - The starting co-ordinate
 * within our grid.
 * @param {number} maxWidth - The maximum width (or index) that our grid is
 * made of.
 * @param {number} maxLength - The maximum length (or lines) that our grid is
 * made of.
 * @returns {Array<{index: number, line: number}>} - A list of valid paths
 * that are within our grid.
 */
const generateMasSearches = (start, maxWidth, maxLength) => {
  let searchPaths = [
      [{ index: start.index - 1, line: start.line - 1}, start, { index: start.index + 1, line: start.line + 1 }], // MAS down right
      [{ index: start.index + 1, line: start.line - 1}, start, { index: start.index - 1, line: start.line + 1 }], // MAS down left
      [{ index: start.index - 1, line: start.line + 1}, start, { index: start.index + 1, line: start.line - 1 }], // MAS up right
      [{ index: start.index + 1, line: start.line + 1}, start, { index: start.index - 1, line: start.line - 1 }], // MAS up left
  ];
  const validPaths = calculateValidPaths(searchPaths, maxWidth, maxLength);
  debugLine(`Found ${validPaths.length} valid MAS cross paths from start point [${start.index},${start.line}]`);
  return validPaths;
}

/**
 * Test whether a provided path of letters results in the expected match
 * string.
 *
 * @param {Array<{index: number, line: number}>} pathToTest The path of
 * co-ordinates to build a string from.
 * @param {Array<string>} lines - The input search space that we're looking
 * through.
 * @param {string} matchString - The final string to match against.
 * @returns {boolean} - True if the created string matches the match string.
 */
const searchFilter = (pathToTest, lines, matchString) => {
  let testString = '';
  pathToTest.forEach((step) => {
    testString += lines[step.line][step.index];
  })
  return testString === matchString;
}

// MAIN CODE STARTS HERE
let lineNumber = 0;

// Split the lines apart.
const lines = fs.readFileSync(args.input).toString('utf-8')
  .split("\n");

// Calculate the maximum size of the grid (ignoring any blank trailing lines)
const maxLength = lines.filter((line) => line.length > 0).length;
const maxWidth = lines[0].length;
const searches = [];

let xmasCount = 0;

if (args.mode === "X") {
  debugLine("X-MAS crossed wordsearch mode...")
  // In X-MAS mode, we're interested in two matches, which then count as a
  // single match.
  lines.forEach((line) => {
    const found = [...line.matchAll(/A/g)];
    for (let idx in found) {
      searches.push(
          generateMasSearches(
              {index: found[idx].index, line: lineNumber},
              maxWidth, maxLength),
      );
    }
    lineNumber += 1;
  });

  debugLine(`${searches.length} valid paths to check...`);
  xmasCount = searches.filter((cross) => {
    return cross.filter((path) => searchFilter(path, lines, 'MAS')).length === 2;
  });
} else {
  debugLine("XMAS default wordsearch mode...");
  // In XMAS mode, we're just interested in as many occurrences of XMAS as we
  // can find.
  lines.forEach((line) => {
    const found = [...line.matchAll(/X/g)];
    for (let idx in found) {
      searches.push(
          ...generateXmasSearches({index: found[idx].index, line: lineNumber},
              maxWidth, maxLength),
      );
    }
    lineNumber += 1;
  });

  debugLine(`${searches.length} valid paths to check...`);
  xmasCount = searches.filter((path) => searchFilter(path, lines, 'XMAS'));
}

console.log(`Valid words found: ${xmasCount.length}`);
