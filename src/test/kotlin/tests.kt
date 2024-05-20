package org.example

import FilePDF
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File


class MainTest {
    @Test
    fun createDirectories(): Unit {

        val inputDir = File("filesToRead")
        val outputDir = File("processedFiles")
        val databaseDir = File("database")

        createDirs()

        assertTrue(inputDir.exists())
        assertTrue(outputDir.exists())
        assertTrue(databaseDir.exists())
    }

    @Test
    fun processPDFFile(): Unit {

        val filepath = this::class.java.classLoader.getResource("pdf_test.pdf")
        val file = File(filepath.file)
        val wordList = mapOf("this" to 1, "test" to 1, "reading" to 1, "pdf" to 1, "files" to 1, "hello" to 2, "world" to 2)
        val processed = false
        val filePDF = FilePDF(file, wordList, processed)

        filePDF.readFile()

        assertEquals(wordList, filePDF.wordList)
        assertTrue(filePDF.processed)
    }
}
