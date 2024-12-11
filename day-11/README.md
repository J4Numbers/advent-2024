# Day 11 - Plutonium Pebbles

> Written in Rust

This is an iteration algorithm that works on a set of inputs. Each input is a positive number (inc.
zero) and each iteration modifies those inputs according to a series of rules. The first rule that
fits is used.

* If a number is `0`, then the number becomes `1`
* If a number has an _even_ number of digits, then it is split in half down the middle into two
  stones (i.e. `1234` becomes `12` and `34`). Any leading zeroes on the right stone are dropped
* If no other rules apply, multiply the number by `2024`

The order of numbers is always preserved and we want to find out the number of stones after a
certain number of iterations.

For example...

For an input of five numbers `0 1 10 99 999`, then the first iteration would do the following:

* The first number becomes `1`
* The second number is multiplied and becomes `2024`
* The third number is split into `1` and `0`
* The fourth number is split into `9` and `9`
* The fifth number is multiplied and becomes `2021976`

Resulting in `7` numbers.

For a longer example...

```text
Initial arrangement:
125 17

After 1 blink:
253000 1 7

After 2 blinks:
253 0 2024 14168

After 3 blinks:
512072 1 20 24 28676032

After 4 blinks:
512 72 2024 2 0 2 4 2867 6032

After 5 blinks:
1036288 7 2 20 24 4048 1 4048 8096 28 67 60 32

After 6 blinks:
2097446912 14168 4048 2 0 2 4 40 48 2024 40 48 80 96 2 8 6 7 6 0 3 2
```

After 6 iterations, there are `22` numbers, and after 25 iterations, there are `55312` numbers.  

## This script

This script uses Rust and can be run with the following command:

```shell
cargo run -- -i input.txt
```

To vary the number of iterations, include the `--iterations <int>` flag, where `<int>` can be any
positive number.

Debug logging can be enabled with `RUST_LOG=debug` as a command prefix.