package dxtractor

import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.name
import kotlin.system.exitProcess

fun main() {
    println("Thank you for running DXtractor to help the linux gaming community")
    println("Fetching your steam libraries...")
    val libraries = IO.extractLibraryPaths()
    println("Fetching your DXVK cache files...")
    val caches = libraries.map { IO.listCacheFiles(it.resolve("steamapps").resolve("shadercache")) }.flatten()
    println("Found the following suitable cache files:")
    caches.forEach { println(" - $it") }
    println("Would you like me to copy the files to your home folder? [Yes/No]")
    when (askCopy()) {
        true -> {
            caches.forEach { Files.copy(it, Path.of(System.getProperty("user.home"), it.name)) }
            println("Done")
        }
        false -> exitProcess(0)
    }
}

/**
 * Ask the user for input
 * @return true when the files should be copied, false when not
 * */
private fun askCopy(): Boolean {
    val scanner = Scanner(System.`in`)
    while (true) {
        val line = scanner.nextLine()
        when (line.lowercase()) {
            "yes" -> return true
            "no" -> return false
        }
    }
}