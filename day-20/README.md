# Day 20 - Race Condition

> Written in Kotlin

This is a mapping algorithm (yet again) with two main differences.

* The map only has one path from the `S`tart to the `E`nd.
* The user can 'cheat' by hopping over a single wall

The puzzle input is presented in a series of fixed-width lines that can only ever contain the 
characters `.#SE`.

Every step counts for a single point. For example, given the following graph:

```text
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############
```

The total number of steps to reach the exit from the start point is `84` without cheats.

If we start using cheats, then there are a number of possible ways to save time.

In this example, the total number of cheats (grouped by the amount of time they save) are as follows:

- There are `14` cheats that save `2` steps
- There are `14` cheats that save `4` steps
- There are `2` cheats that save `6` steps
- There are `4` cheats that save `8` steps
- There are `2` cheats that save `10` steps
- There are `3` cheats that save `12` steps
- There is one cheat that saves `20` steps
- There is one cheat that saves `36` steps
- There is one cheat that saves `38` steps
- There is one cheat that saves `40` steps
- There is one cheat that saves `64` steps

If we consider any cheat that saves us `20` or more seconds as viable, then there are `5` possible
cheats that would match that condition.

## Part two

Part two states that a single cheat can actually last up to `20` steps. This means that the below
solution is now possible:

```text
###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#1#####.#.#.###
#2#####.#.#...#
#3#####.#.###.#
#456.E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############
```

If the start and end position have the same endpoint, then they are classed as the same cheat.

This means that there are now the following solutions for cheats that would save at least `50`
steps:

* There are `32` cheats that save `50` steps
* There are `31` cheats that save `52` steps
* There are `29` cheats that save `54` steps
* There are `39` cheats that save `56` steps
* There are `25` cheats that save `58` steps
* There are `23` cheats that save `60` steps
* There are `20` cheats that save `62` steps
* There are `19` cheats that save `64` steps
* There are `12` cheats that save `66` steps
* There are `14` cheats that save `68` steps
* There are `12` cheats that save `70` steps
* There are `22` cheats that save `72` steps
* There are `4` cheats that save `74` steps
* There are `3` cheats that save `76` steps

To a new total of `285` possible cheats.

## This script

This script uses Kotlin and requires compilation before it can be run:

```shell
mvn clean package
java -jar <program> input.txt <saving-to-beat> <skipped-steps-allowed>
```

For example, `java -jar <program> input.txt 100 2` can be run for part 1.
