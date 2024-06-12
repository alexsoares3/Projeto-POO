// Main.kt
package org.example

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.multiple
import createDirectories
import getAllFiles
import processFiles
import resetDB
import searchDB


// TODO, Add word cloud subcommand, takes number of words as argument
// TODO, Add load command that takes a path argument to load files into database from another directory? Copy/move files to directory?
class MainCommand : CliktCommand() {
    override fun run() = Unit
}

// Name defines the command name, help defines the help message
class SearchCommand : CliktCommand(name = "search", help = "Searches the database for files with matching words. Example: search word1 word2") {
    private val words by argument().multiple()

    override fun run() {
        val searchResult = searchDB(words.joinToString(" "))
        println("Found ${searchResult.size} matching file(s).")
        if (searchResult.isNotEmpty()) {
            println("File Name | Matches | Word count")
            searchResult.forEach { file ->
                println("${file.first} | ${file.second} | ${file.third}")
            }
        }
    }
}

class ListCommand : CliktCommand(name = "list", help = "Lists all files in the database") {
    override fun run() {
        val fileList = getAllFiles()
        println("Found ${fileList.size} matching file(s).")
        if (fileList.isNotEmpty()) {
            println("File Name")
            var count = 0
            fileList.forEach { file ->
                count++
                println("${count}. ${file.first}")
            }
        }
    }
}

class ResetCommand : CliktCommand(name = "reset", help = "Resets the database, gives user the option to delete all files") {
    override fun run() {
        resetDB()
    }
}

fun main(args: Array<String>) {
    // check if reset command is in the arguments
    if (args.isEmpty() || args[0] != "reset") {
        processFiles()
    }

    // run clikt application
    MainCommand()
        .subcommands(SearchCommand(), ListCommand(), ResetCommand())
        .main(args)
}