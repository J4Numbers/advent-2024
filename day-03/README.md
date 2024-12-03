# Day 3 - Mull it over

> Written in Javascript

This problem is a search problem on a large string to extract operations.

The following assumptions can be made about the inputs:

* There are between 1 and many lines

The line contains a series of instructions, such as `mul(1,2)` which describe a multiplication
between two numbers. These operations are only valid if the following are true:
* The pattern _must_ be `mul\([0-9]{1,3},[0-9]{1,3}\)`

Once all these operations are found, we're looking for the sum of all valid operations.

For example... an input of:

```text
xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
```

Only contains four valid instructions - `2*4 + 5*5 + 11*8 + 8*5` - resulting in a total value of
`161`.

## Part two

Two new instructions must be listened to - `do()`, and `don't()` which enable and disable the
`mul()` instruction respectively.

For example:

```text
xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
```

Equates to `48` (`2*4 + 8*5`).

## This script

This script uses JavaScript and can be run with the following command:

```shell
npm i
node src/app.js -i input.txt
```

Which will run the above scenario on the given input file after installing any required packages.

To enable part two, include `--mode enabled`.
