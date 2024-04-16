package org.example

import readPDF
import readPPXT
import java.io.File


fun main() {
    createDirs()
    var menuInput = 0
    menuInput = menu()
    if (menuInput == 1) {
        //readPdf("Aula_2 Introducao_OPP.pdf")
        readPDF()
        readPPXT()
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