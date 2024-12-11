# Day 10 - Hoof It

> Written in GoLang

This is a pathfinding algorithm program to find the number of potential trails through a maze. A
trail starts wherever a `0` appears, and valid trails must move incrementally through `1-9`.

We can make the following assertions about the input file:

* All lines are of a consistent size
* It is in the format of `[0-9]+`

A file contains many trailheads (`0`), and each trailhead is scored by the number of valid paths
that can be made to a unique `9` value (a split path on the trailhead leading to the same `9`
should still count for `1`).

For example...

```text
0123
1234
8765
9876
```

Has a valid trail of:

```text
0123
...4
8765
9...
```

And a few other options that still equate to a score of `1`.

A trail of:

```text
...0...
...1...
...2...
6543456
7.....7
8.....8
9.....9
```

Has a score of `2`

A full example would look more like:

```text
89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732
```

Which has 9 trailheads, of scores `5`, `6`, `5`, `3`, `1`, `3`, `5`, `3`, and `5`. Adding all of
these together results in a final score of `36`.

## Part two

Part two instead requests the number of distinct paths. For the example above, the total score is
now `81`.

## This script

This script uses Go and can be run with the following command:

```shell
go run . -i input.txt
```

To enable part two, add the `--paths` flag.