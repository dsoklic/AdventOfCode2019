package day7

import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

const val ADDITION = "01"
const val MULTIPLICATION = "02"
const val INPUT = "03"
const val OUTPUT = "04"

const val JUMP_IF_TRUE = "05"
const val JUMP_IF_FALSE = "06"
const val LESS_THAN = "07"
const val EQUALS = "08"

const val HALT = "99"
const val FILE_PATH = "input.txt"

class Computer(val inputQueue: Queue<Int>, val outputQueue: Queue<Int>, program: List<Int>) {
    private val programMemory = program.toMutableList()
    private var index = 0
    var running = true;

    private enum class Mode {
        Position,
        Immediate
    }

    fun executeProgram() {
        while (true) {
            val instruction = programMemory[index].toString().padStart(4, '0')
            val opcode = instruction.substring(2)

            val input1Mode = if (instruction[1] == '0') Mode.Position else Mode.Immediate
            val input2Mode = if (instruction[0] == '0') Mode.Position else Mode.Immediate

            when (opcode) {
                ADDITION -> {
                    val inp1 = getInput(programMemory[index + 1], input1Mode, programMemory)
                    val inp2 = getInput(programMemory[index + 2], input2Mode, programMemory)
                    programMemory[programMemory[index + 3]] = inp1 + inp2
                    index += 4
                }
                MULTIPLICATION -> {
                    val inp1 = getInput(programMemory[index + 1], input1Mode, programMemory)
                    val inp2 = getInput(programMemory[index + 2], input2Mode, programMemory)
                    programMemory[programMemory[index + 3]] = inp1 * inp2
                    index += 4
                }
                INPUT -> {
                    if (inputQueue.isEmpty()) {
                        // Queue is empty, stop processing until input is available.
                        return
                    }

                    programMemory[programMemory[index + 1]] = inputQueue.remove()
                    index += 2
                }
                OUTPUT -> {
                    outputQueue.add(getInput(programMemory[index + 1], input1Mode, programMemory))
                    index += 2
                }

                JUMP_IF_TRUE -> {
                    val inp = getInput(programMemory[index + 1], input1Mode, programMemory)
                    if (inp != 0) {
                        index = getInput(programMemory[index + 2], input2Mode, programMemory)
                    } else {
                        index += 3
                    }
                }
                JUMP_IF_FALSE -> {
                    val inp = getInput(programMemory[index + 1], input1Mode, programMemory)
                    if (inp == 0) {
                        index = getInput(programMemory[index + 2], input2Mode, programMemory)
                    } else {
                        index += 3
                    }
                }
                LESS_THAN -> {
                    val inp1 = getInput(programMemory[index + 1], input1Mode, programMemory)
                    val inp2 = getInput(programMemory[index + 2], input2Mode, programMemory)

                    programMemory[programMemory[index + 3]] = if (inp1 < inp2) 1 else 0
                    index += 4
                }
                EQUALS -> {
                    val inp1 = getInput(programMemory[index + 1], input1Mode, programMemory)
                    val inp2 = getInput(programMemory[index + 2], input2Mode, programMemory)

                    programMemory[programMemory[index + 3]] = if (inp1 == inp2) 1 else 0
                    index += 4
                }

                HALT -> {
                    running = false
                    return
                }
                else -> throw IllegalArgumentException("Unexpected opcode: '$opcode'")
            }
        }
    }

    private fun getInput(parameter: Int, mode: Mode, memory: List<Int>): Int = when (mode) {
        Mode.Immediate -> parameter
        Mode.Position -> memory[parameter]
    }
}

fun String.toProgram(): List<Int> = this.split(',').map{ it.toInt() }.toList()

fun amplifiersPart1(phases: List<Int>, program: List<Int>): Int {
    val aInpQ = LinkedList<Int>(listOf(phases[0], 0))
    val abInpQ = LinkedList<Int>(listOf(phases[1]))
    val bcInpQ = LinkedList<Int>(listOf(phases[2]))
    val cdInpQ = LinkedList<Int>(listOf(phases[3]))
    val deInpQ = LinkedList<Int>(listOf(phases[4]))
    val eOutQ = LinkedList<Int>()

    val amplifiers = listOf(
        Computer(aInpQ, abInpQ, program),
        Computer(abInpQ, bcInpQ, program),
        Computer(bcInpQ, cdInpQ, program),
        Computer(cdInpQ, deInpQ, program),
        Computer(deInpQ, eOutQ, program)
    )

    amplifiers.forEach{ it.executeProgram() }

    return eOutQ.remove()
}

//fun amplifiersPart2(phases: List<Int>, program: List<Int>): Int {
//    var signal = 0
//    val ampMemories = (1..5).map { program.toMutableList() }.toList()
//
//    do {
//        val startSignal = signal
//        phases.forEachIndexed { i, phase ->
//            signal = executeProgram(LinkedList(listOf(phase, signal)), ampMemories[i])
//        }
//    } while (startSignal != signal)
//
//    return signal
//}

fun allPhaseCombinations(part1: Boolean): List<List<Int>> {
    var listOfPhases = mutableListOf<List<Int>>()

    val ranges = if (part1) 0..4 else 5..9

    for (a in ranges) {
        for (b in ranges) {
            for (c in ranges) {
                for (d in ranges) {
                    for (e in ranges) {
                        val combination = listOf(a,b,c,d,e)
                        if (combination.sorted().zipWithNext().all { (a, b) -> a != b }) {
                            listOfPhases.add(combination)
                        }
                    }
                }
            }
        }
    }

    return listOfPhases
}

fun main() {
    val program = File("input.txt").readText().toProgram()

    val part1 = allPhaseCombinations(part1 = true).map { Pair(it, amplifiersPart1(it, program)) }.maxBy { it.second }
    println(part1)

//    val testProgram = "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5".toProgram()
//    val part2 = allPhaseCombinations(part1 = false).map { Pair(it, amplifiersPart2(it, testProgram)) }.maxBy { it.second }
//    println(part2)
}