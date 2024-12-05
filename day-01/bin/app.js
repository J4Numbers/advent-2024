#!/usr/bin/env node

import yargs from 'yargs';
import { hideBin } from 'yargs/helpers';

import * as fs from 'fs';

const argsEngine = yargs(hideBin(process.argv));
const args = argsEngine.wrap(argsEngine.terminalWidth())
  .env('J4_ADVENT_01')
  .options({
    input: {
      alias: 'i',
      type: 'string',
      description: 'The input file to run this algorithm on',
      demandOption: true,
    },
    mode: {
      options: ['distance', 'similarity'],
      default: 'distance',
      description: 'Choose between calibrating on distance between the lists, or their similarity',
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

// Basic variables
const leftList = [];
const rightList = [];
const lineRegex = /([0-9]+)\s{3}([0-9]+)/;

/**
 * @param line - Print this line if console debug is enabled
 */
const debugLine = (line) => {
  if (debug) {
    console.log(line);
  }
};

/**
 * Given a line in our input file, split it out into the left and right
 * list numbers. If the line is invalid (or empty), provide a blanked 0
 * pair.
 *
 * @param {string} line - The input line we're scanning over.
 * @returns {{left: number, right: number}} - The returned left and right
 * list numbers, respectively.
 */
const splitDigits = (line) => {
  const output = lineRegex.exec(line);
  let left = 0;
  let right = 0;
  if (output) {
    left = parseInt(output[1], 10);
    right = parseInt(output[2], 10);
  } else {
    debugLine(`Unable to match regex on line '${line}'`);
  }
  return {
    left,
    right,
  };
};

/**
 * Calculate the distance between sorted lists by using absolute values
 * when comparing numbers.
 *
 * @param left - The left list which is sorted in a consistent order to
 * the right.
 * @param right - The right list which is sorted in a consistent order
 * to the left.
 * @returns {number} - The final additive distance between the two lists.
 */
const calculateDistance = (left, right) => {
  let count = 0;
  for (let i in left) {
    count += Math.abs(left[i] - right[i]);
  }
  return count;
};

/**
 * Calculate how similar the two lists are to each other. Do this with the
 * aid of a cache in case we have many duplicate items in the left list.
 *
 * Similarity is calculated by how often the value appears in each list as
 * a multiple of that count, all added together after going over each list.
 *
 * @param left - The left list which is sorted in a consistent order to
 * the right.
 * @param right - The right list which is sorted in a consistent order to
 * the left.
 * @returns {number} - The final similarity score between the two lists.
 */
const calculateSimilarity = (left, right) => {
  const cache = {};
  let count = 0;
  for (let i in left) {
    if (Object.keys(cache).includes(left[i])) {
      debugLine(`Cache hit on ${left[i]} => ${cache[left[i]]}`);
    } else {
      cache[left[i]] = left[i] * right
        .filter((item) => item === left[i])
        .length;
      debugLine(`Cache miss on ${left[i]} => ${cache[left[i]]}`);
    }
    count += cache[left[i]];
  }
  return count;
}

// MAIN CODE STARTS HERE

// Read individual lines of the file one by one
const file = fs.readFileSync(args.input).toString('utf-8');
file.split('\n')
  .forEach((line) => {
    let {left, right} = splitDigits(line);

    leftList.push(left);
    rightList.push(right);
  });

// Default sorting is enough
leftList.sort();
rightList.sort();

let count = 0;

// If we're operating on distance difference, run that function
if (args.mode === 'distance') {
  count = calculateDistance(leftList, rightList);
}
// And if we're on list similarity, choose that function instead
if (args.mode === 'similarity') {
  count = calculateSimilarity(leftList, rightList);
}

// Return the final value to the user.
console.log(count);
