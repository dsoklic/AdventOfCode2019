package day4

const val START = 356261
const val END = 846303

fun passwordValid(password: Int): Boolean {
    val passwordPairs = password.toString().zipWithNext();

    if (!passwordPairs.any { (a, b) -> a == b }) {
        // Two numbers are not in sequence.
        return false;
    }

    if(!passwordPairs.all { (a, b) -> a <= b }) {
        return false;
    }

    return true;
}

fun passwordValidPart2(password: Int): Boolean {
    val passwordString = password.toString()
    val sequences = mutableListOf<Int>()

    var currentChar = passwordString[0]
    var sequenceLength = 1

    for (i in 1 until passwordString.length) {
        val newChar = passwordString[i]
        if (currentChar == newChar) {
            sequenceLength++
        } else {
            sequences.add(sequenceLength)

            currentChar = newChar
            sequenceLength = 1
        }
    }

    sequences.add(sequenceLength)

    return sequences.any{ it == 2 }
}

fun main() {
    val passwordOptions = (START..END);
    val part1 = passwordOptions.count { passwordValid(it) }
    val part2 = passwordOptions.filter { passwordValid(it) }.count { passwordValidPart2(it) }
    println("Part 1: $part1")
    println("Part 2: $part2")
}
