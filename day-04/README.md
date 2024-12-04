# Day 4 - Ceres Search

> Written in Javascript

This problem is a wordsearch of sorts to find all occurrences of a string.

The following assumptions can be made about the inputs:

* There are between 1 and many lines
* Only letters `[XMAS]` appear in this input
* All lines are an equal length.

The word to look for in the wordsearch is 'XMAS'. This can appear in any of the 8 cardinal
directions from the first letter (i.e. forwards, backwards, diagonally, vertically), and we
want to find out how many times this word appears in the whole wordsearch.

For example...

```text
MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX
```

Can be simplified down to:

```text
....XXMAS.
.SAMXMS...
...S..A...
..A.A.MS.X
XMASAMX.MM
X.....XA.A
S.S.S.S.SS
.A.A.A.A.A
..M.M.M.MM
.X.X.XMASX
```

Where XMAS has only appeared `18` times.

## Part 2 - X-MAS

Instead of searching for the string 'XMAS', we must instead search for two MAS strings which
intersect and can _only_ intersect in the shape X - i.e.

```text
M.S
.A.
M.S
```

The example above now can be simplified down to: 

```text
.M.S......
..A..MSMS.
.M.S.MAA..
..A.ASMSM.
.M.S.M....
..........
S.S.S.S.S.
.A.A.A.A..
M.M.M.M.M.
..........
```

Where an X-MAS appears `9` times.

## This script

This script uses JavaScript and can be run with the following command:

```shell
npm i
node src/app.js -i input.txt
```

Which will run the above scenario on the given input file after installing any required packages.

To change operations to part 2 - add the flag `--mode X`.
