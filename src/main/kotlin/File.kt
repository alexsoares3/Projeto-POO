import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.io.FileNotFoundException

abstract class File_base(var path: File, var wordList: Map<String, Int>?) {
    abstract fun readFile()

    //Splits the text in lines, removes non-word characters, removes spaces and converts to lowercase. Adds and counts words in a Map<Word, NrTimes>
    protected fun processWords(text: String) {

        val wordCountMap = mutableMapOf<String, Int>()

        // Split the text into lines
        val lines = text.split("\n")

        for (line in lines) {
            val words = line.split("\\W+".toRegex()) // Split by non-word characters

            for (word in words) {
                val sanitizedWord = word.trim().lowercase() // Remove whitespace and convert to lowercase
                if (sanitizedWord.isNotEmpty() && sanitizedWord.length > 2) { // Check if it's not empty
                    if (!wordCountMap.containsKey(sanitizedWord)) { // Check if the word is already in the dictionary, if not add it and set its value to 1
                        wordCountMap[sanitizedWord] = 1
                    } else {
                        // Add 1 to existing word
                        wordCountMap[sanitizedWord] = wordCountMap[sanitizedWord]!! + 1
                    }
                }
            }
        }
        wordList = wordCountMap
    }
}

fun moveFiles() {
    val inputDir = File("filesToRead")
    val outputDir = File("processedFiles")

    // Create the destination directory if it doesn't exist
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }

    // List files in the source directory
    val files = inputDir.listFiles()

    // Move each file to the destination directory
    files?.forEach { file ->
        val destinationFile = File(outputDir, file.name)

        try {
            Files.move(file.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
        } catch (e: FileNotFoundException) {
            println("File not found: ${file.name}.")
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}

