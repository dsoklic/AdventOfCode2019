package day3

import java.io.File
import java.lang.IllegalArgumentException

const val FILE_PATH = "input.txt"

data class MoveInstruction(val direction: Char, val distance: Int)
data class Coordinate(val x: Int, val y: Int)

fun List<MoveInstruction>.toPath(): List<Coordinate> {
    var currentX = 0
    var currentY = 0

    var path = mutableListOf<Coordinate>()

    for (instruction in this) {
        for (i in 0 until instruction.distance) {
            when (instruction.direction) {
                'R' -> currentX++
                'L' -> currentX--
                'U' -> currentY++
                'D' -> currentY--
                else -> throw IllegalArgumentException("Wasn't expecting direction $instruction.direction")
            }

            path.add(Coordinate(currentX, currentY))
        }
    }

    return path
}

fun List<Coordinate>.distanceToCoordinate(coordinate: Coordinate): Int = indexOfFirst { it == coordinate } + 1

fun Coordinate.manhattanDistanceToCenter(): Int = kotlin.math.abs(x) + kotlin.math.abs(y)

fun main() {
    val individualLines = File(FILE_PATH).readLines()

    val paths = individualLines.map {
        it.split(',').map {innerIt -> MoveInstruction(
            direction = innerIt[0],
            distance = innerIt.substring(1).toInt()
    )}.toPath() }

    val path1 = paths[0]
    val path2 = paths[1]

    val intersections = path1.intersect(path2)

    val part1 = intersections.map(Coordinate::manhattanDistanceToCenter).min()
    val part2 = intersections.asSequence().map { path1.distanceToCoordinate(it) + path2.distanceToCoordinate(it) }.min()

    println("Part 1 $part1")
    println("Part 2 $part2")
}