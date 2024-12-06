# Day 5 - Print Queue

> Written in Javascript

This problem is a rule engine that works in two parts. The first is a series of rules in the
format: `X|Y`, where `X` must appear before `Y` in the second part to be considered a valid
list.

The second part of the input is a list of numbers, some of which matching the rules listings.

The following assumptions can be made about the inputs:

* Between 1 and many lines are in the format `[0-9]+\|[0-9]+`
* Between 1 and many lines are in the format `[0-9]+(,[0-9]+)+`
* All numbers are positive.

The task is twofold:

Figure out which number lists are valid with the provided rules, then add up the middle number
in each of them to reach the final output.

For example...

```text
47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47
```

Can be simplified down to:

```text
75,47,61,53,29
97,61,53,29,13
75,29,13
```

As correctly ordered inputs that follow the listed rules above. Adding the middle number of all
three inputs results in a correct answer of `143`.

## Part two

Since we've inspected the list for valid cases, we now look to the invalidly applied lists and
consider what it would take to make them valid.

After turning them into valid lists, we must them also return the sum of all middle index items
from those lists.

For example... from the three incorrect lists from part one:

* `75,97,47,61,53` becomes `97,75,47,61,53`
* `61,13,29` becomes `61,29,13`
* `97,13,75,29,47` becomes `97,75,47,29,13`

And their new lists sum their middle index to `123`

## This script

This script uses JavaScript and can be run with the following command:

```shell
npm i
node src/app.js -i input.txt
```

Which will run the above scenario on the given input file after installing any required packages.

This script will return the responses to both parts of the question above.
