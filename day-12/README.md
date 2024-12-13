# Day 12 - Garden Groups

> Written in Rust

This is a region mapping algorithm that calculates the sizes of fence regions. Regions are given
by their area and their perimeter, and the price to fence a region is its area multiplied by its
perimeter.

Overlapping regions have separate fences.

The following assertions can be made about the file we read in:

* All lines are of a fixed length
* All lines only contain the characters `[A-Z]+`
* Letters are not unique throughout a file and can belong to multiple regions

For example...

```text
AAAA
BBCD
BBCC
EEEC
```

Has five distinct regions:
* `A`, with an area of `4` and a perimeter of `10`
* `B`, with an area of `4` and a perimeter of `8`
* `C`, with an area of `4` and a perimeter of `10`
* `D`, with an area of `1` and a perimeter of `4`
* And `E`, with an area of `3` and a perimeter of `8`

This results in the total cost to this region being `4(10) + 4(8) + 4(10) + 1(4) + 3(8) = 140`.

For a larger example...

```text
RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE
```

Has a total region cost of `1930`

## Part two

Part two states that straight lines count for a single side.

The example above now has a total cost of `1206`.

## This script

This script uses Rust and can be run with the following command:

```shell
cargo run -- -i input.txt
```

Debug logging can be enabled with `RUST_LOG=debug` as a command prefix.