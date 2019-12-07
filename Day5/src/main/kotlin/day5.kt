package day5


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

enum class Mode {
    Position,
    Immediate
}

fun executeProgram(inputValue: Int, programMemory: MutableList<Int>) {
    var index = 0

    while (true) {
        val instruction = programMemory[index].toString().padStart(4, '0')
        val opcode = instruction.substring(2)

        val input1Mode = if (instruction[1] == '0') Mode.Position else Mode.Immediate
        val input2Mode = if (instruction[0] == '0') Mode.Position else Mode.Immediate

        when (opcode) {
            ADDITION -> {
                val inp1 = getInput(programMemory[index+1], input1Mode, programMemory)
                val inp2 = getInput(programMemory[index+2], input2Mode, programMemory)
                programMemory[programMemory[index+3]] = inp1 + inp2
                index += 4
            }
            MULTIPLICATION -> {
                val inp1 = getInput(programMemory[index+1], input1Mode, programMemory)
                val inp2 = getInput(programMemory[index+2], input2Mode, programMemory)
                programMemory[programMemory[index+3]] = inp1 * inp2
                index += 4
            }
            INPUT -> {
                programMemory[programMemory[index+1]] = inputValue
                index += 2
            }
            OUTPUT -> {
                val output = getInput(programMemory[index+1], input1Mode, programMemory)
                index += 2
                println("Output: $output")
            }

            JUMP_IF_TRUE -> {
                val inp = getInput(programMemory[index+1], input1Mode, programMemory)
                if (inp != 0) {
                    index = getInput(programMemory[index+2], input2Mode, programMemory)
                } else {
                    index += 3
                }
            }
            JUMP_IF_FALSE -> {
                val inp = getInput(programMemory[index+1], input1Mode, programMemory)
                if (inp == 0) {
                    index = getInput(programMemory[index+2], input2Mode, programMemory)
                } else {
                    index += 3
                }
            }
            LESS_THAN -> {
                val inp1 = getInput(programMemory[index+1], input1Mode, programMemory)
                val inp2 = getInput(programMemory[index+2], input2Mode, programMemory)

                programMemory[programMemory[index+3]] = if (inp1 < inp2) 1 else 0
                index += 4
            }
            EQUALS -> {
                val inp1 = getInput(programMemory[index+1], input1Mode, programMemory)
                val inp2 = getInput(programMemory[index+2], input2Mode, programMemory)

                programMemory[programMemory[index+3]] = if (inp1 == inp2) 1 else 0
                index += 4
            }

            HALT -> return
            else -> throw IllegalArgumentException("Unexpected opcode: '$opcode'")
        }
    }
}

fun getInput(parameter: Int, mode: Mode, memory: List<Int>): Int = when (mode) {
    Mode.Immediate -> parameter
    Mode.Position -> memory[parameter]
}

fun main() {
    val input = File(FILE_PATH).readText().split(',').map{ it.toInt() }.toMutableList()
    executeProgram(1, input)
    println("---- part 2 -----")
    executeProgram(5, input)
}