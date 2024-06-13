package org.example
import getAllFiles
import getConnection
import processFiles
import searchDB
import createDirectories
import java.io.File
import kotlin.system.exitProcess

//Neste momento ao chamarem processFiles() ele le todos os ficheiros na pasta e atribui a classe conforme a extensao. Ao ser atribuido a uma
// classe vai ser feita automaticamente a leitura/extracao do texto e define o campo wordList com a lista/nr de palavras. Depois exporta para a db
//Podem usar os ficheiros em testSample.rar para testar

//TODO Remove this whole page since right now its only for testing (cli implemented)

fun main() {
    //Create necessary directories on startup
    createDirectories()

    while (true) {
        val menuInput = menu()
        if (menuInput == 1) {
            // processFiles() > readFile() from all objects > processWords() from all objects > moveFiles() > insertFilesInDB()
            processFiles(null)
            //getAllFiles() to get every file available in the database
            println(getAllFiles())

        } else if (menuInput == 2) {
            //Search
            println("Search for: ")
            val searchQuery = readln()
            val searchResult = searchDB(searchQuery)
            println("Found ${searchResult.size} matching files.")
            if (searchResult.isNotEmpty()) {
                println("File Name | Matches | Word count")
                searchResult.forEach { file ->
                    println("${file.first} | ${file.second} | ${file.third}")
                }
            }

        } else if (menuInput == 3) {
            //Close app
            exitProcess(0)
        }
    }
}

fun menu(): Int {

    var userInput = 0

    while (userInput !in 1..3) {
        println("---MENU---")
        println("1 - Adicionar ficheiros a BD")
        println("2 - Procurar")
        println("3 - Sair")

        try {
            userInput = readLine()?.toInt() ?: 0
        } catch (e: NumberFormatException) {
            println("Escolha uma das opcoes disponiveis.")
        }
    }

    return userInput
}

