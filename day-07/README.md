# Day 7 - Bridge Repair

> Written in GoLang

This is a computational problem to check whether a statement holds against an assumption. The
statement is that a test value must be able to be computed by adding or multiplying together the
constituent parts provided.

The following assumptions can be made about the inputs:

* All lines are in the format `[0-9]+:( [0-9]+)+`
* Only positive numbers can appear

Only addition and multiplication can be used to check whether a set of input parameters can ever
match the test value. Once we have a list of valid statements, we add together all the valid
test values.

For example...

```text
190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20
```

Only three of the above equations can be made true by inserting operators:

* `190: 10 19` has only one position that accepts an operator: between `10` and `19`. Choosing `+`
  would give `29`, but choosing `*` would give the test value `(10 * 19 = 190)`
* `3267: 81 40 27` has two positions for operators. Of the four possible configurations of the
  operators, two cause the right side to match the test value: `81 + 40 * 27` and `81 * 40 + 27`
  both equal `3267` (when evaluated left-to-right)!
* `292: 11 6 16 20` can be solved in exactly one way: `11 + 6 * 16 + 20`

With the above example, that makes the sum of all possible equations `3749`.

## Part two

An additional operator is included - `||` - which is a join between two numbers to make a new
number. For example: `11 || 2 = 112`. Operators still resolve left to right, meaning that another
example of `11 || 2 * 2 = 224`.

That updates the above example with the three additional valid equations:

* `156: 15 6` can be made true through a single concatenation: `15 || 6 = 156`
* `7290: 6 8 6 15` can be made true using `6 * 8 || 6 * 15`
* `192: 17 8 14` can be made true using `17 || 8 + 14`

This brings the new total sum to `11387`

## This script

This script uses Go and can be run with the following command:

```shell
go run . -i input.txt
```

To include the join operator, add `--join` to the command.
