package org.example

import readPdf

fun main() {
    var menuInput = 0
    menuInput = menu()
    if (menuInput == 1) {
        //readPdf("Aula_2 Introducao_OPP.pdf")
        readPdf()
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