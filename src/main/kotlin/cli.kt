// Main.kt
package org.example

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import createDirectories
import processFiles
import searchDB

class MainCommand : CliktCommand() {
    init {
        //Create necessary directories on startup
        createDirectories()
        // Process files on startup
        processFiles()
    }
    override fun run() = Unit
}

// Name defines the command name, help defines the help message
class SearchCommand : CliktCommand(name = "search", help = "Searches the database for files with matching words. Example: search word1 word2") {
    private val words by argument().multiple()

    override fun run() {
        val searchResult = searchDB(words.joinToString(" "))
        println("Found ${searchResult.size} matching files.")
        if (searchResult.isNotEmpty()) {
            println("File Name | Matches | Word count")
            searchResult.forEach { file ->
                println("${file.first} | ${file.second} | ${file.third}")
            }
        }
    }
}

fun main(args: Array<String>) = MainCommand()
    .subcommands(SearchCommand())
    .main(args)