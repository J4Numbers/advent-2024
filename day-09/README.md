# Day 9 - Disk Fragmenter

> Written in GoLang

This is an optimisation program that takes in a single line that describes how a disk has been
laid out, and should first optimise that disk into leaving no free space, and then calculate a
checksum from the resulting disk.

We can make the following assertions about the input file:

* The input file contains a single line
* It is in the format of `[0-9]+`

The file is made of pairs of numbers, where the first number in a pair describes how long a file
is, then the second number describes how much free space should be left until the next file occurs.

For example, `12345` describes one block of a file, then two blocks of free space, then three
blocks of a second file, and 4 blocks of free space, and finally 5 blocks of a third file. Each of
these files are given an 0-indexed ID number by order of appearance, meaning the actual disk
probably looks like:

```text
0..111....22222
```

Optimisation is carried out by taking the last block and inserting it to the first block of free
space in the disk, until there are no free space gaps...

For example:

```text
0..111....22222
02.111....2222.
022111....222..
0221112...22...
02211122..2....
022111222......
```

After all of this, we then calculate the checksum of the disk by multiplying the position (0-index)
in the disk by the file ID that is stored there, and adding all of thsoe values together.

For example, the above would produce:

```text
0 * 0 = 0
1 * 2 = 2
2 * 2 = 4
3 * 1 = 3
4 * 1 = 4
5 * 1 = 5
6 * 2 = 12
7 * 2 = 14
8 * 2 = 16
```

Which totals together to give the result of `59`

Another example would be:

```text
2333133121414131402
```

Which produces a total checksum of `1928`

## Part two

Instead of fragmenting file blocks apart, only move a file if it fits in the free space that is
available.

This is explicitly a reverse lookup at this point, as we do not work left to right and move files
about once space has been made up elsewhere.

With the example input of `2333133121414131402`, the file movement becomes the following:

```text
00...111...2...333.44.5555.6666.777.888899
0099.111...2...333.44.5555.6666.777.8888..
0099.1117772...333.44.5555.6666.....8888..
0099.111777244.333....5555.6666.....8888..
00992111777.44.333....5555.6666.....8888..
```

With a new checksum value of `2858`

## This script

This script uses Go and can be run with the following command:

```shell
go run . -i input.txt
```

To enable part two, use `--complete`
