# Day 17 - Chronospatial Computer

> Written in Kotlin

This is an assembly emulation program that takes in information about an operating system and
produces the printed values.

The input is presented as a set of three registers, `A`, `B`, and `C`, which can hold any number,
and then with a sequential program made up of many instructions made up of opcodes and operands.

All instructions are in base 8, and each instruction is executed sequentially.

Operands can either be literal `0-7` values, or combo operands which are one of the following:
* `0-3` as literal values `0,1,2,3`
* `4` as the contents of register `A` 
* `5` as the contents of register `B` 
* `6` as the contents of register `C`

`7` will never appear as a valid combo operand.

The following are valid instructions and their meanings:

* `0` - `adv` meaning division, where the numerator is the contents of the `A` register, and the 
  denominator is 2 to the power of the combo operand value. I.e. `0, 5` would be `2^B`. The result
  is truncated to an integer and written to `A`.
* `1` - `bxl` meaning bitwise XOR of register `B` with the literal operand as an input - the result
  is stored in register `B`.
* `2` - `bst` meaning register `B` is now written with the value of its combo operand, modulo `8`.
* `3` - `jnz` does nothing if the value in `A` is `0`, however, if it's not zero, then it jumps the
  instruction pointer to the value of its _literal_ operand and does not increment it afterwards
* `4` - `bxc` calculates the bitwise XOR of register `B` _and_ `C` together and stores it in
  register `B`. An operand is provided but should be ignored.
* `5` - `out` calculates the value of its combo operand, modulo `8`, then outputs that value
  (multiple `out` commands should be separated by commas)
* `6` - `bdv` is identical to `adv`, except the output is stored in the `B` register (the `A`
  register is still used as the input)
* `7` - `cdv` is identical to `adv`, except the output is stored in the `C` register (the `A`
  register is still used as the input)

Regarding the input, we can say that the file will always be in the following format:

```regexp
Register A: [0-9]+
Register B: [0-9]+
Register C: [0-9]+

Program: ([0-7],[0-7])+
```

Where all registers are given a starting value, and the program is always an even number of 3-bit
numbers.

For example, given the following input:

```text
Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0
```

The instructions are as follows:

* `adv` - `729 / 2^1 = 364.5 ~ 364 = A`
* `out` - `PRINT 364 % 8 = 4`
* `jmp` - if `A(364) > 0` - `restart from 0`
* `adv` - `364 / 2^1 = 182 = A`
* `out` - `PRINT 182 % 8 = 6`
* `jmp` - if `A(182) > 0` - `restart from 0`
* `adv` - `182 / 2^1 = 91 = A`
* `out` - `PRINT 91 % 8 = 3`
* `jmp` - if `A(91) > 0` - `restart from 0`
* `adv` - `91 / 2^1 = 45.5 ~ 45 = A`
* `out` - `PRINT 45 % 8 = 5`
* `jmp` - if `A(45) > 0` - `restart from 0`
* `adv` - `45 / 2^1 = 22.5 ~ 22 = A`
* `out` - `PRINT 22 % 8 = 6`
* `jmp` - if `A(22) > 0` - `restart from 0`
* `adv` - `22 / 2^1 = 11 = A`
* `out` - `PRINT 11 % 8 = 3`
* `jmp` - if `A(11) > 0` - `restart from 0`
* `adv` - `11 / 2^1 = 5.5 ~ 5 = A`
* `out` - `PRINT 5 % 8 = 5`
* `jmp` - if `A(5) > 0` - `restart from 0`
* `adv` - `5 / 2^1 = 2.5 ~ 2 = A`
* `out` - `PRINT 2 % 8 = 2`
* `jmp` - if `A(2) > 0` - `restart from 0`
* `adv` - `2 / 2^1 = 1 = A`
* `out` - `PRINT 1 % 8 = 1`
* `jmp` - if `A(1) > 0` - `restart from 0`
* `adv` - `1 / 2^1 = 0.5 ~ 0 = A`
* `out` - `PRINT 0 % 8 = 0`
* `jmp` - if `A(0) > 0` - DO NOTHING

And the final printed output should be: `4,6,3,5,6,3,5,2,1,0`

## Part two

Part two instead requests that we search for the lowest value in register A that would produce
the same output as the original program.

## This script

This script uses Kotlin and requires compilation before it can be run:

```shell
mvn clean package
java -jar <program> input.txt
```
