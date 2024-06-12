package org.example

import FileDOCX
import FilePDF
import FilePPTX
import FileTXT
import createDirectories
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File


class MainTest {
    @Test
    fun createDirectoriesTest(): Unit {

        val directories = createDirectories()
        val inputDir = directories["input"]
        val outputDir = directories["output"]
        val databaseDir = directories["database"]


        assertTrue(inputDir!!.exists())
        assertTrue(outputDir!!.exists())
        assertTrue(databaseDir!!.exists())
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

    @Test
    fun processTXTFile(): Unit{

        val filepath = this::class.java.classLoader.getResource("txt_test.txt")
        val file = File(filepath.file)
        val wordList = mapOf("lets" to 1, "test" to 1, "reading" to 1, "txt" to 1, "files" to 1, "hello" to 2, "world" to 2)
        val process = false
        val fileTXT = FileTXT(file, wordList, process)

        fileTXT.readFile()

        assertEquals(wordList, fileTXT.wordList)
        assertTrue(fileTXT.processed)
    }

    @Test
    fun processPPTXFile(): Unit{

        val filepath = this::class.java.classLoader.getResource("pptx_test.pptx")
        val file = File(filepath.file)
        val wordList = mapOf("lets" to 1, "test" to 1, "reading" to 1, "pptx" to 1, "files" to 1, "hello" to 2, "world" to 2)
        val process = false
        val filePPTX = FilePPTX(file, wordList, process)

        filePPTX.readFile()

        assertEquals(wordList, filePPTX.wordList)
        assertTrue(filePPTX.processed)
    }

    @Test
    fun processDocx(): Unit {

        val filepath = this::class.java.classLoader.getResource("docx_test.docx")
        val file = File(filepath.file)
        val wordList = mapOf("lets" to 1, "test" to 1, "reading" to 1, "docx" to 1, "files" to 1, "hello" to 2, "world" to 2)
        val process = false
        val fileDOCX = FileDOCX(file, wordList, process)

        fileDOCX.readFile()

        assertEquals(wordList, fileDOCX.wordList)
        assertTrue(fileDOCX.processed)
    }
}
