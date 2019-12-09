package day9

import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

const val FILE_PATH = "input.txt"

class Computer(val inputQueue: Queue<Long>, val outputQueue: Queue<Long>, program: List<Long>) {
    private val ADDITION = "01"
    private val MULTIPLICATION = "02"
    private val INPUT = "03"
    private val OUTPUT = "04"

    private val JUMP_IF_TRUE = "05"
    private val JUMP_IF_FALSE = "06"
    private val LESS_THAN = "07"
    private val EQUALS = "08"

    private val RELATIVE_BASE_SET = "09"

    private val HALT = "99"

    private val programMemory = program.toMutableList()
    private var index = 0
    private var relativeBase: Long = 0

    var running = true;

    private enum class Mode {
        Position,
        Immediate,
        Relative
    }

    fun executeProgram() {
        while (true) {
            val instruction = programMemory[index].toString().padStart(5, '0')
            val opcode = instruction.takeLast(2)

            val input1Mode = instruction[2].toParamMode()
            val input2Mode = instruction[1].toParamMode()
            val outputMode = instruction[0].toParamMode()

            when (opcode) {
                ADDITION -> {
                    val inp1 = getInput(programMemory[index + 1], input1Mode)
                    val inp2 = getInput(programMemory[index + 2], input2Mode)
                    setOutput(programMemory[index + 3], inp1 + inp2, outputMode)
                    index += 4
                }
                MULTIPLICATION -> {
                    val inp1 = getInput(programMemory[index + 1], input1Mode)
                    val inp2 = getInput(programMemory[index + 2], input2Mode)
                    setOutput(programMemory[index + 3], inp1 * inp2, outputMode)
                    index += 4
                }
                INPUT -> {
                    if (inputQueue.isEmpty()) {
                        // Queue is empty, stop processing until input is available.
                        return
                    }

                    setOutput(programMemory[index + 3], inputQueue.remove(), outputMode)
                    index += 2
                }
                OUTPUT -> {
                    outputQueue.add(getInput(programMemory[index + 1], input1Mode))
                    index += 2
                }

                JUMP_IF_TRUE -> {
                    val inp = getInput(programMemory[index + 1], input1Mode)
                    if (inp != 0L) {
                        index = getInput(programMemory[index + 2], input2Mode).toInt()
                    } else {
                        index += 3
                    }
                }
                JUMP_IF_FALSE -> {
                    val inp = getInput(programMemory[index + 1], input1Mode)
                    if (inp == 0L) {
                        index = getInput(programMemory[index + 2], input2Mode).toInt()
                    } else {
                        index += 3
                    }
                }
                LESS_THAN -> {
                    val inp1 = getInput(programMemory[index + 1], input1Mode)
                    val inp2 = getInput(programMemory[index + 2], input2Mode)

                    val value = if (inp1 < inp2) 1L else 0L
                    setOutput(programMemory[index + 3], value, outputMode)
                    index += 4
                }
                EQUALS -> {
                    val inp1 = getInput(programMemory[index + 1], input1Mode)
                    val inp2 = getInput(programMemory[index + 2], input2Mode)

                    val value = if (inp1 == inp2) 1L else 0L
                    setOutput(programMemory[index + 3], value, outputMode)
                    index += 4
                }
                RELATIVE_BASE_SET -> {
                    val inp1 = getInput(programMemory[index + 1], input1Mode)

                    relativeBase += inp1
                    index += 2
                }

                HALT -> {
                    running = false
                    return
                }
                else -> throw IllegalArgumentException("Unexpected opcode: '$opcode'")
            }
        }
    }

    private fun Char.toParamMode(): Mode = when (this) {
        '0' -> Mode.Position
        '1' -> Mode.Immediate
        '2' -> Mode.Relative
        else -> throw IllegalArgumentException("Unexpected parameter mode: $this")
    }

    private fun getInput(parameter: Long, mode: Mode): Long = when (mode) {
        Mode.Immediate -> parameter
        Mode.Position -> programMemory.getOrElse<Long>(parameter.toInt()) { 0L }
        Mode.Relative -> programMemory.getOrElse<Long>((relativeBase + parameter).toInt()) { 0L }
    }

    private fun setOutput(position: Long, value: Long, mode: Mode) {
        // Check if memory is large enough
        val index = when (mode) {
            Mode.Position -> position.toInt()
            Mode.Relative -> (position + relativeBase).toInt()
            else -> throw IllegalArgumentException("Unexpected move when setting output: $mode")
        }

        if (index >= programMemory.size) {
            for (i in 0..(index - programMemory.size)) {
                programMemory.add(0)
            }
        }

        programMemory[index] = value
    }
}

fun String.toProgram(): List<Long> = this.split(',').map{ it.toLong() }.toList()

fun main() {
//    val test = "109,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99".toProgram()
//    val test = "1102,34915192,34915192,7,4,7,99,0".toProgram()

    val program = File("input.txt").readText().toProgram()

    // Part 1
    run {
        val inputQ = LinkedList<Long>(listOf(1L))
        val outputQ = LinkedList<Long>()

        val computer = Computer(inputQ, outputQ, program);
        computer.executeProgram()

        while (outputQ.isNotEmpty()) {
            println(outputQ.remove())
        }
    }

    // Part 2
    run {
        val inputQ = LinkedList<Long>(listOf(2L))
        val outputQ = LinkedList<Long>()

        val computer = Computer(inputQ, outputQ, program);
        computer.executeProgram()

        while (outputQ.isNotEmpty()) {
            println(outputQ.remove())
        }
    }
}
