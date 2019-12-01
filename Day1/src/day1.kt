package day1

import java.io.File

const val FILE_PATH = "input.txt"

fun calculateFuel(mass: Int): Int = mass / 3 - 2

fun calculateFuelWithAdditionalFuel(mass: Int): Int {
    var allFuelRequired = 0
    var massNeededToFuel = mass

    while (true) {
        val fuel = calculateFuel(massNeededToFuel)

        if (fuel <= 0) {
            break;
        }

        allFuelRequired += fuel
        massNeededToFuel = fuel
    }

    return allFuelRequired
}

fun main() {
    val fileLines: List<Int> = File(FILE_PATH).readLines().map { x -> x.toInt() }

    val fuelPart1 = fileLines.map { mass -> calculateFuel(mass) }
                             .sum()

    println("Part 1: Fuel required: $fuelPart1")

    val fuelPart2 = fileLines.map { mass -> calculateFuelWithAdditionalFuel(mass) }
                             .sum()

    println("Part 2: Fuel required: $fuelPart2")
}