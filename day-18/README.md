# Day 18 - RAM run

> Written in Kotlin

This is another maze running simulation where the input is the position of blockages on a defined
grid size.

The input is presented as pairs of coordinates where blocks have been laid, in the format of `x,y`
where both `x` and `y` are positive integers.

Additional inputs to the program should include the size of the grid (as a single positive number
describing the X and Y axis limit), and the number of lines to read from the input before stopping
and executing the maze runner algorithm.

## Part two

Part two is the answer to the question of 'which byte causes the path to fail'. We can figure this
out relatively easily with no further adjustments by performing a binary search of inputs to pin
down the exact moment when the input fails to reach a solution.

## This script

This script uses Kotlin and requires compilation before it can be run:

```shell
mvn clean package
java -jar <program> input.txt <axisLength> <inputLinesToRead>
```
