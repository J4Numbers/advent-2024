# Day 1 - Historian Hysteria

> Written in Javascript

This problem is a sorting and calculation-based puzzle based on a big list of many numbers.

The following assumptions can be made about the inputs:

* There are between 1 and many lines
* All lines are in the format of `[0-9]+ {3}[0-9]+`

Each line consists of two numbers, with the left number marking an item in list 1, and the right
number representing items in list 2.

The desire should be to create two distinct lists and iterate over them once they are sorted to
calculate the positive difference between each of the lists.

for example... an input of:

```text
3   4
4   3
2   5
1   3
3   9
3   3
```

Generates a total distance between the two lists of `11`.

## This script

This script uses JavaScript and can be run with the following command:

```shell
npm i
node src/app.js -i input.txt
```

Which will run the above scenario on the given input file after installing any required packages.

## Part two - Similarity score

Part two instead posits that what we're looking for is how often items in the left list appear in
the right list.

For this, we instead say that for every time a number appears in the left list, we multiply it by
the number of times it appears in the right list, adding all the different similaritiy scores
together to reach a final result:

Therefore, the test input above instead makes `31`.

### This script

To run this script in similarity mode, use:

```shell
node src/app.js -i input.txt --mode similarity
```
