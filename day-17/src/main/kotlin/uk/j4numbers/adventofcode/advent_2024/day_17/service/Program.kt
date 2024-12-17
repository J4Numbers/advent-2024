package uk.j4numbers.adventofcode.advent_2024.day_17.service

import uk.j4numbers.adventofcode.advent_2024.day_17.logger
import uk.j4numbers.adventofcode.advent_2024.day_17.pojo.MiniComputer
import kotlin.math.pow

class Program constructor(private val miniComputer: MiniComputer) {
    private var regA: Long = 0;
    private var regB: Long = 0;
    private var regC: Long = 0;

    private var instructionCounter: Int = 0;
    private var output: MutableList<Int> = mutableListOf();

    init {
        this.regA = miniComputer.regA;
        this.regB = miniComputer.regB;
        this.regC = miniComputer.regC;
    }

    fun clean(regA: Long) {
        this.regA = regA;
        this.regB = 0;
        this.regC = 0;

        this.instructionCounter = 0;
        this.output.clear();
    }

    private fun lookupComboOperand(operand: Int): Number {
        return when (operand) {
            0 -> { operand }
            1 -> { operand }
            2 -> { operand }
            3 -> { operand }
            4 -> { regA }
            5 -> { regB }
            6 -> { regC }
            else -> { 0 }
        }
    }

    private fun performInstruction(opcode: Int, operand: Int) {
        when (opcode) {
            0 -> {
                val partA = "adv - ${this.regA} / 2^${lookupComboOperand(operand)}"
                this.regA = Math.floorDiv(this.regA, 2.toDouble().pow(lookupComboOperand(operand).toDouble()).toLong())
                logger.trace { "$partA >> ${this.regA}" }
                instructionCounter += 2;
            }
            1 -> {
                val partA = "bxl - ${this.regB} XOR $operand"
                this.regB = this.regB xor operand.toLong();
                logger.trace { "$partA >> ${this.regB}" }
                instructionCounter += 2;
            }
            2 -> {
                val partA = "bst - ${lookupComboOperand(operand)} % 8"
                this.regB = lookupComboOperand(operand).toLong() % 8;
                logger.trace { "$partA >> ${this.regB}" }
                instructionCounter += 2;
            }
            3 -> {
                val partA = "jnz - ${this.regA} > 0 ?"
                if (regA > 0) {
                    logger.trace { "$partA Y > GOTO $operand" }
                    instructionCounter = operand;
                } else {
                    logger.trace { "$partA N > CONTINUE" }
                    instructionCounter += 2;
                }
            }
            4 -> {
                val partA = "bxc - ${this.regB} XOR ${this.regC}"
                this.regB = this.regB xor this.regC;
                logger.trace { "$partA >> ${this.regB}" }
                instructionCounter += 2;
            }
            5 -> {
                val partA = "out - ${lookupComboOperand(operand)} % 8 >> ${lookupComboOperand(operand).toLong() % 8}"
                output.add((lookupComboOperand(operand).toLong() % 8).toInt())
                logger.trace { partA }
                instructionCounter += 2;
            }
            6 -> {
                val partA = "bdv - ${this.regA} / 2^${lookupComboOperand(operand)}"
                this.regB = Math.floorDiv(this.regA, 2.toDouble().pow(lookupComboOperand(operand).toDouble()).toLong())
                logger.trace { "$partA >> ${this.regB}" }
                instructionCounter += 2;
            }
            7 -> {
                val partA = "cdv - ${this.regA} / 2^${lookupComboOperand(operand)}"
                this.regC = Math.floorDiv(this.regA, 2.toDouble().pow(lookupComboOperand(operand).toDouble()).toLong())
                logger.trace { "$partA >> ${this.regC}" }
                instructionCounter += 2;
            }
        }
    }

    fun executeProgram(): List<Int> {
        while (instructionCounter < miniComputer.instructions.size -1) {
            performInstruction(
                miniComputer.instructions[instructionCounter],
                miniComputer.instructions[instructionCounter+1],
            )
        }

        return output;
    }

    private fun generateRangeMap (base: Long): Map<Int, List<Int>> {
        val newMap = mutableMapOf<Int, MutableList<Int>>()
        logger.debug { "Generating range map for octet on base value $base" };
        for (n in 0..7) {
            this.clean(n.toLong() + base);
            val out = this.executeProgram()[0]
            if (!newMap.containsKey(out)) {
                newMap[out] = mutableListOf();
            }
            newMap[out]!!.add(n);
        }
        logger.debug { newMap };
        return newMap;
    }

    fun reverseFromInstructions(): Long {
        var workingOptions: List<Long> = mutableListOf(0);
        val nextOptions: MutableList<Long> = mutableListOf();

        for (n in miniComputer.instructions.indices.reversed()) {
            val resToLookup = miniComputer.instructions[n];

            for (option in workingOptions.indices) {
                val test = workingOptions[option] shl 3;
                val resMap = generateRangeMap(test);
                if (resMap.containsKey(resToLookup)) {
                    resMap[resToLookup]!!.forEach { opt ->
                        logger.debug { "From option $test, found valid path to next print $resToLookup - $opt" };
                        nextOptions.add(test + opt);
                    }
                }
            }

            workingOptions = nextOptions.toList();
            nextOptions.clear();
        }

        logger.debug { "Found ${workingOptions.size} final options for reversed instructions -- $workingOptions" };
        val accurateOptions = workingOptions.filter { opt ->
            this.clean(opt);
            this.executeProgram() == this.miniComputer.instructions;
        }
        logger.debug { "Found ${accurateOptions.size} accurate options for reversed instructions -- $accurateOptions" };
        return workingOptions.fold(Long.MAX_VALUE) { lowest, opt -> if (opt < lowest) opt else lowest };
    }
}
