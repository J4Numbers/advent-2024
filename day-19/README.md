# Day 19 - Linen Layout

> Written in Kotlin

This is a jigsaw puzzle of sorts, which provides a series of possible puzzle pieces, and the
different puzzles that must be made with those pieces.

In particular, the puzzle input is split into two:
* The first part is the available pieces in the format of `([wubrg]+)(,[wubrg]+)+`
* The second part is the puzzles we must make from the pieces across several lines in the format
  of `[wubrg]+`.

For example, given the puzzle pieces `w`, `u`, `b`, and `ub`, then the puzzle of `wub` could be
created from `w`, `u`, and `b`, or from `w`, and `ub`.

We would like to know how many of the puzzles are possible, given the pieces provided. For example,
given the input of:

```text
r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb
```

Then `6` of the puzzles above are possible, while `ubwu` and `bbrgwb` are impossible to make with
the pieces we have available.

## Part two

Part two also asks for the total number of combinations possible for forming the provided patterns.
The example above has `16` total combinations to make the `6` puzzles.

## This script

This script uses Kotlin and requires compilation before it can be run:

```shell
mvn clean package
java -jar <program> input.txt
```
