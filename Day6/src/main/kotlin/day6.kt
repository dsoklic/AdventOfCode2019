package day6

import java.io.File

const val FILE_PATH = "input.txt"

fun Map<String, List<String>>.countOrbits(parentName: String = "COM", level: Int = 0): Int =
    (this[parentName]?.map { countOrbits(it, level + 1) }?.sum() ?: 0) + level

fun Map<String, List<String>>.findPath(childName: String,
                                       currentParent:String = "COM",
                                       pathThusFar:List<String> = listOf("COM")): List<String>? {
    if (currentParent == childName) {
        return pathThusFar
    }

    return this[currentParent]?.mapNotNull { findPath(childName, it, pathThusFar.toList() + it) }?.firstOrNull()
}

fun Map<String, List<String>>.countTransfers(a: String, b:String): Int {
    val pathToA = findPath(a)!!
    val pathToB = findPath(b)!!

    val lastIntersection = pathToA.intersect(pathToB).last()

    val jumpsFromAToIntersect = pathToA.size - pathToA.indexOf(lastIntersection) - 1
    val jumpsFromBToIntersect = pathToB.size - pathToB.indexOf(lastIntersection) - 1
    return jumpsFromAToIntersect + jumpsFromBToIntersect
}

fun main() {
    val solarSystem: Map<String, List<String>> = File(FILE_PATH).readLines()
        .filter { it.isNotEmpty() }
        .map { it.split(')') }
        .groupBy({it[0]}, {it[1]})

    val part1 = solarSystem.countOrbits()
    println("Part1 $part1")

    val part2 = solarSystem.countTransfers("YOU", "SAN") - 2 // Remove 2 because YOU and SAN don't count.
    println(part2)
}


