package day2

import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

const val ADDITION = 1
const val MULTIPLICATION = 2
const val HALT = 99
const val FILE_PATH = "input.txt"

fun executeProgram(programMemory: MutableList<Int>): List<Int> {
    val programCommands: Queue<Int> = LinkedList(programMemory)

    while (programCommands.isNotEmpty()) {
        val opcode = programCommands.remove()!!

        if (opcode == HALT) {
            return programMemory
        }

        val input1 = programMemory[programCommands.remove()!!]
        val input2 = programMemory[programCommands.remove()!!]
        val outputLocation: Int = programCommands.remove()!!

        programMemory[outputLocation] = when (opcode) {
            ADDITION -> input1 + input2
            MULTIPLICATION -> input1 * input2
            else -> throw IllegalArgumentException("Unexpected opcode: $opcode")
        }
    }

    return programMemory
}

fun computeFirstNumber(noun: Int, verb: Int, input: List<Int>): Int {
    val inputCopy = input.toMutableList()

    inputCopy[1] = noun
    inputCopy[2] = verb

    return executeProgram(inputCopy)[0]
}

fun product(first: IntRange, second: IntRange): List<Pair<Int, Int>> {
    val prod = mutableListOf<Pair<Int, Int>>()
    for (e in first) {
        for (f in second) {
            prod.add(Pair(e, f))
        }
    }
    return prod
}

fun main() {
    val input = File(FILE_PATH).readText().split(',').map { x -> x.toInt() }.toList()

    val output1 = computeFirstNumber(12, 2, input)
    println("Part 1: $output1")

    val (noun, verb) = product(0..99, 0..99).first{ computeFirstNumber(it.first, it.second, input) == 19690720 }
    val output2 = 100 * noun + verb
    println("Part 2: $output2")
}