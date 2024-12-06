# Day 6 - Guard Gallivant

> Written in GoLang

This is a pathing problem for finding how many unique locations were visited from a starting
point while working towards exiting the grid, all via an algorithm.

The following assumptions can be made about the inputs:

* All lines are of a constant length
* The only characters that can appear in any of the lines are `[.#^]`
* There is only ever one occurrence of `^`

The algorithm states that the starting position is marked by `^`, and the person is always facing
up at the start. They continue forwards until they encounter an obstacle (`#`), then they turn to
the right and continue. This repeats until they exit the grid.

Once the person exits our grid, we then count the number of unique positions they have been.

For example...

```text
....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#...
```

Once the person has finished moving around, the grid will look like the following, where `X` is
a visited space.

```text
....#.....
....XXXXX#
....X...X.
..#.X...X.
..XXXXX#X.
..X.X.X.X.
.#XXXXXXX.
.XXXXXXX#.
#XXXXXXX..
......#X..
```

Totalling up the number of unique visited squares results in a correct answer of `41`.

## Part two

Part two now posits that we want to form an infinite loop where the person will continue walking
in the same circle forever, with the addition of a single obstacle.

More importantly, we want to know how many possible permutations we could have where the
introduction of one obstacle results in an infinite loop.

For the example above, the new answer is now `6`.

## This script

This script uses Go and can be run with the following command:

```shell
go run . -i input.txt
```

This will return an answer for both part one and part two of the problem above.
