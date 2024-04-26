package org.example

import File_base
import processFiles
import java.io.File

//Neste momento ao chamarem processFiles() ele le todos os ficheiros na pasta e atribui a classe conforme a extensao. Ao ser atribuido a uma
// classe vai ser feita automaticamente a leitura/extracao do texto e define o campo wordList com a lista/nr de palavras. Ja nao exporta o texto para .txt


fun main() {
    createDirs()

    var menuInput = 0
    menuInput = menu()
    if (menuInput == 1) {

    val list = processFiles()

    println(list)

    list.forEach { file ->
        file.readFile()
        println("${file.path.path} - ${file.wordList}")
    }


    } else if (menuInput == 3) {
        return
    }
}

fun menu(): Int {

    var userInput = 0

    while (userInput !in 1..3) {
        println("---MENU---")
        println("1 - Adicionar ficheiros a BD")
        println("2 - Procurar")
        println("3 - Sair")

        userInput = readln().toInt()
    }

    return userInput
}

fun createDirs() {
    val inputDir = File("filesToRead")
    val outputDir = File("extractedText")

    // Check if input directory exists and is a directory
    if (!inputDir.exists() || !inputDir.isDirectory) {
        inputDir.mkdirs()
    }

    // Check if output directory exists or create it if it doesn't
    if (!outputDir.exists() || !outputDir.isDirectory) {
        outputDir.mkdirs()
    }
}