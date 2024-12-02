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
    dampen: {
      type: 'boolean',
      default: false,
      description: 'How many bad stages can exist in the file before counting it as invalid',
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
const toDampen = args.dampen;

// Base variables
const numberRegex = /[0-9]+/g;

/**
 * @param line - Print this line if console debug is enabled
 */
const debugLine = (line) => {
  if (debug) {
    console.log(line);
  }
};

/**
 * Validate whether an array provided to us is a valid report. A report
 * must fit three criteria to be considered valid. There must be at least
 * 2 entries in the report. All entries must trend up or down. And each
 * entry must be at most 3 (and at least 1) away from the prior entry.
 *
 * @param {Array<number>} listToTest - The report to test.
 * @returns {boolean} - Return true if the report is considered as valid.
 */
const testValidReport = (listToTest) => {
  let valid = listToTest.length > 1;
  let badCount = 0;
  if (valid) {
    let lastDiff;
    for (let i=1; i<listToTest.length; ++i) {
      let thisDiff = listToTest[i] - listToTest[i-1];
      if (lastDiff !== undefined) {
        if (Math.sign(thisDiff) !== Math.sign(lastDiff)) {
          valid = false;
          break;
        }
      }
      if (Math.abs(thisDiff) < 1 || Math.abs(thisDiff) > 3) {
        valid = false;
        break;
      }
      lastDiff = thisDiff;
    }
  }
  debugLine(`List [${listToTest}] marked as ${valid ? 'valid' : 'invalid'}`);
  return valid;
}

/**
 * For a 'good-enough' approach, explode the report we're given out into
 * different permutations, depending on if we're enabling dampening (i.e.
 * allowing failure if one item in the array is missing).
 *
 * @param {Array<number>} reportToExplode - The report line to explode into
 * different permutations.
 * @param {boolean} dampen - Whether to dampen the report input or not.
 * @returns {Array<Array<number>>} - An array of the possible report
 * permutations we can have.
 */
const explodeReport = (reportToExplode, dampen) => {
  // By default, we only have the base report
  let reportsToTest = [reportToExplode];
  if (dampen) {
    // If dampening is enabled, generate an array where each value in
    // sequence has been removed.
    for (let i = 0; i < reportToExplode.length; ++i) {
      reportToExplode.reduce(
          (acc, _v, idx) => {
            acc.push(reportToExplode.toSpliced(idx, 1));
            return acc;
          },
          reportsToTest
      );
    }
  }
  return reportsToTest;
}

// MAIN CODE STARTS HERE

let validCount = 0;

// Read individual lines of the file one by one
const file = fs.readFileSync(args.input).toString('utf-8');
file.split('\n')
  .forEach((line) => {
    // Find all numbers on a line and put them into an array.
    let report = [...line.matchAll(numberRegex)].map(
        (match) => parseInt(match, 10));

    debugLine(`Found report [${report}] from line '${line}'`);

    // Explode the report into its different permutations.
    let explodedReports = explodeReport(report, toDampen);

    // For each report, test if it's valid.
    for (let reportIdx in explodedReports) {
      if (testValidReport(explodedReports[reportIdx])) {
        validCount += 1;
        break;
      }
    }
  });

console.log(`Valid reports found: ${validCount}`);

