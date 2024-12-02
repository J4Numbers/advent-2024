# Day 1 - Red-Nosed Reports

> Written in Javascript

This problem is a logical expression applied to a list of numbers.

The following assumptions can be made about the inputs:

* There are between 1 and many lines
* All lines are in the format of `[0-9]+( [0-9]+)+`

Each line contains a list of numbers. A line is marked _valid_ if the following two conditions
hold true:
* All numbers in the line are either reducing or increasing in order
* Any two adjacent numbers differ by at least 1 and at most 3

We are only interested in the _count_ of valid reports.

For example... an input of:

```text
7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9
```

Only contains two valid reports on the first and last lines, making the valid count `2`.

## Part two - Dampening

Part two allows for one bad stage per report. As long as it's only one bad stage, then that
report can be classed as valid by discounting that bad stage entirely.

In the example above, this transforms the valid count into `4`, with lines 4 and 5 becoming
valid.

> The additional edge case of `1 3 2 3 4` should also be classed as valid, as the first `3` is
> able to be removed to make it valid.

## This script

This script uses JavaScript and can be run with the following command:

```shell
npm i
node src/app.js -i input.txt
```

Which will run the above scenario on the given input file after installing any required packages.

To enable part two, add the `--dampen` flag.
