package org.example
import getAllFiles
import getConnection
import processFiles
import searchDB
import java.io.File
import kotlin.system.exitProcess

//Neste momento ao chamarem processFiles() ele le todos os ficheiros na pasta e atribui a classe conforme a extensao. Ao ser atribuido a uma
// classe vai ser feita automaticamente a leitura/extracao do texto e define o campo wordList com a lista/nr de palavras. Depois exporta para a db
//Podem usar os ficheiros em testSample.rar para testar


fun main() {
    createDirs()

    while (true) {
        val menuInput = menu()
        if (menuInput == 1) {
            // processFiles() > readFile() from all objects > processWords() from all objects > moveFiles() > insertFilesInDB()
            processFiles()
            //getAllFiles() to get every file available in the database
            println(getAllFiles())

        } else if (menuInput == 2) {
            //Search
            println("Search for: ")
            val searchQuery = readln()
            val searchResult = searchDB(searchQuery)
            println("Found ${searchResult.size} matching files.")
            if (searchResult.isNotEmpty()) {
                searchResult.forEach { file ->
                    println(file)
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

fun createDirs() { //Create necessary directories (on startup)
    val inputDir = File("filesToRead")
    val outputDir = File("processedFiles")

    // Check if input directory exists and is a directory
    if (!inputDir.exists() || !inputDir.isDirectory) {
        inputDir.mkdirs()
    }

    // Check if output directory exists or create it if it doesn't
    if (!outputDir.exists() || !outputDir.isDirectory) {
        outputDir.mkdirs()
    }
}