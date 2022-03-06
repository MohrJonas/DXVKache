package dxtractor

import java.nio.file.Files
import java.nio.file.Path

/**
 * Object containing IO operations
 * */
object IO {

    /**
     * List all DXVK cache files
     * @param root the root of the search tree
     * @return a list of [java.nio.file.Path]s, representing the found cache files
     * */
    fun listCacheFiles(root: Path) =
        root
            .walk()
            .filter { it.isFile && it.canRead() && it.extension == "dxvk-cache" }
            .map { it.toPath() }
            .toList()

    /**
     * Extract the steam-library paths out of the steam-config
     * @return a list of [java.nio.file.Path]s, representing the found libraries
     * */
    fun extractLibraryPaths() =
        Files
            .readAllLines(Path.of(System.getProperty("user.home"), ".steam", "root", "config", "libraryfolders.vdf"))
            .filter { it.matches(Regex("^\\s{2}\"path\"\\s*\"\\S+\"\$")) }
            .map { Path.of(Regex("^\\s{2}\"path\"\\s*\"(\\S+)\"\$").find(it)!!.groupValues[1]) }
            .filter { Files.exists(it.resolve("steamapps").resolve("shadercache")) }
            .toList()
}

/**
 * Walk methode for [java.nio.file.Path], Equivalent to [java.io.File]
 * */
private fun Path.walk() = this.toFile().walk()
