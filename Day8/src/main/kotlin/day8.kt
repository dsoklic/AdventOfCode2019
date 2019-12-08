package day8

import java.io.File

const val WIDTH = 25
const val HEIGHT = 6

typealias Layer = List<List<Int>>
typealias Image = List<Layer>

fun String.toImage(width: Int, height: Int): Image =
    this.chunked(1).map { it.toInt() }.chunked(width).chunked(height)

fun Layer.count(needle: Int): Int = this.flatten().count { x -> x == needle }

fun Image.layerWithFewest(number: Int): Layer =
    this.map { Pair(it, it.count(number)) }.minBy { it.second }!!.first

fun Image.pixelInLayers(x: Int, y:Int): List<Int> = this.map { layer -> layer[y][x]}.toList()
fun List<Int>.firstVisiblePixel(): Int = this.first { it != 2 }

fun main() {
    val input = File("input.txt").readText().toImage(WIDTH, HEIGHT)
    val layerWithFewestZeros = input.layerWithFewest(0)
    val part1 = layerWithFewestZeros.count(1) * layerWithFewestZeros.count(2)
    println("Part 1: $part1")

    println()
    // part 2
    for (y in 0 until HEIGHT) {
        for (x in 0 until WIDTH) {
            val white = input.pixelInLayers(x, y).firstVisiblePixel() != 0
            print(if (white) 'X' else ' ')
        }
        println()
    }
}