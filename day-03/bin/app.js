#!/usr/bin/env node

import yargs from 'yargs';
import { hideBin } from 'yargs/helpers';

import * as fs from 'fs';

const argsEngine = yargs(hideBin(process.argv));
const args = argsEngine.wrap(argsEngine.terminalWidth())
  .env('J4_ADVENT_1')
  .options({
    input: {
      alias: 'i',
      type: 'string',
      description: 'The input file to run this algorithm on',
      demandOption: true,
    },
    mode: {
      options: ['all', 'enabled'],
      default: 'all',
      description: 'Choose between listening to all mul instructions, or only enabled ones',
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
const mulInstRegex = /mul\(([0-9]{1,3}),([0-9]{1,3})\)/g;
const mulEnabledInstRegex = /mul\(([0-9]{1,3}),([0-9]{1,3})\)|don't\(\)|do\(\)/g;

/**
 * @param line - Print this line if console debug is enabled
 */
const debugLine = (line) => {
  if (debug) {
    console.log(line);
  }
};

/**
 * Find all mul(x,y) instructions within a given file and calculate their
 * product when found.
 *
 * @param {string} fileToSearch - A string to search over for all multiply
 * instructions.
 * @returns {number[]} - An array of numbers which represents the found
 * instructions and their respective products.
 */
const findAllMulInstructions = (fileToSearch) => {
  return [...fileToSearch.matchAll(mulInstRegex)].map(
      (match) => parseInt(match[1], 10) * parseInt(match[2], 10),
  );
};

/**
 * Find all _enabled_ instructions within a given file, where an enabled
 * instruction must be specified with 'do()', and disabled instructions by
 * 'don't()'
 *
 * @param {string} fileToSearch - A string to search over for all valid
 * instructions.
 * @returns {number[]} - An array of only valid instructions and their
 * respective products.
 */
const findAllEnabledMulInstructions = (fileToSearch) => {
  let enabledInstr = true;
  const filteredInstrs = [...fileToSearch.matchAll(mulEnabledInstRegex)]
    .filter((match) => {
      // We don't want to include meta-instructions in our final list, so
      // act on them, but then filter them out.
      if (match[0] === 'do()') {
        enabledInstr = true;
        return false;
      } else if (match[0] === 'don\'t()') {
        enabledInstr = false;
        return false;
      }
      // Otherwise, return whether we're currently accepting instructions.
      return enabledInstr;
    });
  return filteredInstrs.map(
      (match) => parseInt(match[1], 10) * parseInt(match[2], 10),
  );
};

// MAIN CODE STARTS HERE

const file = fs.readFileSync(args.input).toString('utf-8');

let validMuls;

if (args.mode === "enabled") {
  validMuls = findAllEnabledMulInstructions(file);
} else {
  validMuls = findAllMulInstructions(file);
}

debugLine(`Found valid instructions [${validMuls}] from file`);

// Collapse the instructions into one number
const validCount = validMuls.reduce((total, curr) => total + curr, 0);

console.log(`Valid reports found: ${validCount}`);

