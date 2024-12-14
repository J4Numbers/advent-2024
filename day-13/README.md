# Day 13 - Claw Contraption

> Written in Rust

This is a linear algorithm problem where X and Y require solving. The puzzle input is a series of
pre-conditions and a success condition in sets of 3. The pre-conditions describe the behaviour of
two buttons, and the success condition describes the required values of X and Y.

We can simplify the three lines into the following set of three patterns:

```regexp
Button A: X\+[0-9]+, Y\+[0-9]+
Button B: X\+[0-9]+, Y\+[0-9]+
Prize: X=[0-9]+, Y=[0-9]+
```

For example...

```text
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400
```

Describes that button `A` increments `X` by `94` and `Y` by `34`, and that button `B` increments
`X` by `22`, and `Y` by `67`.

The success condition is when `X` is `8400` and `Y` is `5400`.

This can be done by pressing button `A` `80` times, and button `B` `40` times
(`80*94 + 40*22 = 8400` for `X`, and `80*34 + 40*67 = 5400` for `Y`). Each press of button `A`
costs `3` tokens, and each push of button `B` costs `1` token.

Therefore, the cost of this equation is `280` tokens.

If a prize cannot be won within `100` presses of either button, then the prize can be considered
impossible to get.

The total answer is the number of tokens required to win all possible prizes in an input list.

For a larger example:

```text
Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279
```

Has a cost of `480` tokens to win the first and third prizes.

## Part two

Part two says that the prizes are now actually increased by a value of `10000000000000`.

## This script

This script uses Rust and can be run with the following command:

```shell
cargo run -- -i input.txt
```

Debug logging can be enabled with `RUST_LOG=debug` as a command prefix.

To enable part two, include the `--extend` flag.
