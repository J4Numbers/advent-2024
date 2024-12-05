#!/usr/bin/env node

import yargs from 'yargs';
import { hideBin } from 'yargs/helpers';

import * as fs from 'fs';

const argsEngine = yargs(hideBin(process.argv));
const args = argsEngine.wrap(argsEngine.terminalWidth())
  .env('J4_ADVENT_05')
  .options({
    input: {
      alias: 'i',
      type: 'string',
      description: 'The input file to run this algorithm on',
      demandOption: true,
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
const ruleRegex = /^([0-9]+)\|([0-9]+)$/
const listRegex = /^([0-9]+)(,([0-9]+)+)+$/

const rules = [];
const registeredLists = [];

/**
 * @param line - Print this line if console debug is enabled
 */
const debugLine = (line) => {
  if (debug) {
    console.log(line);
  }
};

/**
 * Reduce down a list of rules to only those which are relevant to the items
 * within the provided list. This means rules where both their first and last
 * properties are within the provided list.
 *
 * @param {Array<number>} list - The list of numbers that we want to find all
 * relevant rules for.
 * @param {Array<{first: number, last: number}>} rules - A list of rules to use
 * when tracking the order of a given list, documenting which number should
 * appear first, and which should appear last.
 * @returns {Array<{first: number, last: number}>} - The list of rules which
 * are directly relevant to the provided list.
 */
const reduceToValidRuleset = (list, rules) => {
  return rules.filter((rule) => list.filter((item) => item === rule.first || item === rule.last).length > 1);
};

/**
 * A modified sorting algorithm to help track the order of two numbers when
 * provided a set of rules which describe their position.
 *
 * @param {number} a - The first number to check the order of.
 * @param {number} b - The second number to check the order of.
 * @param {Array<{first: number, last: number}>} rules - The list of rules to
 * apply when performing this sort.
 * @returns {number} - Positive if a occurs after b, negative if b occurs
 * after a, and 0 if the two numbers have no specific ordering against each
 * other.
 */
const ruleSort = (a, b, rules) => {
  let bAfterA = rules.filter((rule) => rule.first === a && rule.last === b).length;
  let aAfterB = rules.filter((rule) => rule.first === b && rule.last === a).length;
  return aAfterB - bAfterA;
};

/**
 * Helper reduction function to return the total of all middle items in a
 * list.
 *
 * @param {number} acc - Ongoing total.
 * @param {Array<number>} list - List of numbers to fetch the middle item from.
 * @returns {number} - The updated total.
 */
const middleItemTotalReduction = (acc, list) => {
  return list[Math.floor(list.length / 2)] + acc
};

// Split the lines apart.
fs.readFileSync(args.input).toString('utf-8')
  .split("\n")
  .forEach((line) => {
    if (ruleRegex.test(line)) {
      // For rules, split them out into what should occur first and what
      // should occur last.
      const rule = ruleRegex.exec(line);
      rules.push({
        first: parseInt(rule[1], 10),
        last: parseInt(rule[2], 10),
      });
    } else if (listRegex.test(line)) {
      // For actual lists we need to check the validity of, turn them into
      // numeric lists we can actually act on.
      registeredLists.push(
          [...line.matchAll(/[0-9]+/g)]
              .map((item) => parseInt(item, 10)),
      );
    }
  });

debugLine(`${rules.length} rules found to operate over ${registeredLists.length} lists...`);

const validLists = [];
const invalidLists = [];

registeredLists.forEach((list) => {
  // Reduce the rules down to only what matters for us.
  const validRules = reduceToValidRuleset(list, rules);
  debugLine(`${list.toString()} includes ${validRules.length} valid rules...`);

  // And test whether all rules hold true with the current sort.
  if (validRules.filter((rule) => list.indexOf(rule.first) > list.indexOf(rule.last)).length === 0) {
    validLists.push(list);
  } else {
    // If not, then add the sorted list to our tracker for invalid lists.
    invalidLists.push(list.sort((a, b) => ruleSort(a, b, validRules)));
  }
});

debugLine(`${validLists.length} valid lists discovered...`);
const validTotal = validLists.reduce(middleItemTotalReduction, 0);

debugLine(`${invalidLists.length} invalid lists which have been remediated...`);
const invalidTotal = invalidLists.reduce(middleItemTotalReduction, 0);

console.log(`From ${validLists.length} valid lists, the total valid count is ${validTotal}`);
console.log(`From ${invalidLists.length} valid lists, the total valid count is ${invalidTotal}`);
