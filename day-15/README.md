# Day 15 - Warehouse Woes

> Written in Rust

This is a progressive algorithm that looks to describe the state of play after a series of
instructions. A map is described within the input data, consisting of walls `#`, boxes `O`, empty
space `.`, and a robot `@`.

The instructions are made up of up `^`, down `v`, left `<`, and right `>` and are included below
the map within the input data across a few lines.

We can therefore break the puzzle input down into two parts:

Part one is the map input and states the following:

* All lines are of an equal length
* The only characters that can appear include `#.O@`
* Only one `@` symbol can appear within a given input

Part two is the instruction set that the robot will be carrying out. This one is much simpler:

* All instruction lines occur after the map description
* The only characters that can appear include `^v<>`

When the robot attempts to move according to its instructions, any boxes in its way will also be
moved according to the direction provided. If there is no room to move those boxes (i.e. a wall
is in the way), then the instruction will be discarded.

The input we are given can be said to fit the following pattern on a per-line basis:

For example, given the following instruction set and map...

```text
########
#..O.O.#
##@.O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

<^^>>>vv<v>>v<<
```

The following maps describe how movements are carried out:

```text
Initial state:
########
#..O.O.#
##@.O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

Move <:
########
#..O.O.#
##@.O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

Move ^:
########
#.@O.O.#
##..O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

Move ^:
########
#.@O.O.#
##..O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

Move >:
########
#..@OO.#
##..O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

Move >:
########
#...@OO#
##..O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

Move >:
########
#...@OO#
##..O..#
#...O..#
#.#.O..#
#...O..#
#......#
########

Move v:
########
#....OO#
##..@..#
#...O..#
#.#.O..#
#...O..#
#...O..#
########

Move v:
########
#....OO#
##..@..#
#...O..#
#.#.O..#
#...O..#
#...O..#
########

Move <:
########
#....OO#
##.@...#
#...O..#
#.#.O..#
#...O..#
#...O..#
########

Move v:
########
#....OO#
##.....#
#..@O..#
#.#.O..#
#...O..#
#...O..#
########

Move >:
########
#....OO#
##.....#
#...@O.#
#.#.O..#
#...O..#
#...O..#
########

Move >:
########
#....OO#
##.....#
#....@O#
#.#.O..#
#...O..#
#...O..#
########

Move v:
########
#....OO#
##.....#
#.....O#
#.#.O@.#
#...O..#
#...O..#
########

Move <:
########
#....OO#
##.....#
#.....O#
#.#O@..#
#...O..#
#...O..#
########

Move <:
########
#....OO#
##.....#
#.....O#
#.#O@..#
#...O..#
#...O..#
########
```

For a larger example:

```text
##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
```

After all the instructions have been carried out, the grid would look like this:

```text
##########
#.O.O.OOO#
#........#
#OO......#
#OO@.....#
#O#.....O#
#O.....OO#
#O.....OO#
#OO....OO#
##########
```

To calculate the score of this grid, each box is scored as its distance from the top multiplied
by 100, plus the distance from the left. I.e. A box in position `1,4` has a total score of `104`.

The whole graph above has a score of `10092` (and the shorter example has a score of `2028`).

## Part two

Part two doubles the width of the warehouse with the following conversions:
* `.` becomes `..`
* `#` becomes `##`
* `O` becomes `[]`
* `@` becomes `@.`

It is now possible for the robot to push two boxes with one box (i.e. in the following example)

```text
........
..[][]..
...[]...
...@....
```

The above boxes are all pushes up when `^` is provided as an instruction.

The score of a box is now calculated with only the left side, and this now results in a new score
for the first example of `9021`.

```text
####################
##[].......[].[][]##
##[]...........[].##
##[]........[][][]##
##[]......[]....[]##
##..##......[]....##
##..[]............##
##..@......[].[][]##
##......[][]..[]..##
####################
```

## This script

This script uses Rust and can be run with the following command:

```shell
cargo run -- -i input.txt
```

Debug logging can be enabled with `RUST_LOG=debug` as a command prefix.

To enable part two, include the `--scale` flag.
