import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

//Reads all files in the input folder and assigns them classes based on their extension
// processFiles() > readFile() from all objects > processWords() from all objects > moveFiles() > insertFilesInDB()
// Every time a File is instantiated the File.readFile() runs, then File.processWords() runs. Files are then moved to a different folder.
// Finally files are added to the database in tables files(id, fileName) and file_words(file_id,word,count)
fun processFiles() {
    val inputDir = File("filesToRead") //Input directory
    val outputDir = File("processedFiles") //Input directory
    val fileList: MutableList<File_base> = mutableListOf()

    val outputDirList = outputDir.listFiles()?.map { it.name } ?: emptyList()

    val skipTracker = mutableListOf<String>()

    //iterate through all files in dir
    inputDir.listFiles()?.forEach { file ->

        if (file.isFile && !outputDirList.contains(file.name)) { //verify if normal file and if file is already processed
            if (file.extension.equals("pdf", ignoreCase = true)) {
                fileList += FilePDF(file, null, false)

            } else if (file.extension.equals("pptx", ignoreCase = true)) {
                fileList += FilePPTX(file, null, false)
            } else if (file.extension.equals("docx", ignoreCase = true)) {
                fileList += FileDOCX(file, null, false)
            }
        } else {
            skipTracker += file.name
        }

    }
    if (skipTracker.isNotEmpty()) {
        println("Skipped ${skipTracker.size} files.")
    }
    // Move processed files to output directory
    moveFiles(fileList)

    // Insert processed files in database
    insertFilesInDB(fileList)
}

fun moveFiles(processedFiles: List<File_base>) {
    val outputDir = File("processedFiles")

    // Create the destination directory if it doesn't exist
    if (!outputDir.exists()) {
        outputDir.mkdirs()
    }

    // Iterate through processed files and move them
    processedFiles.forEach { file ->
        if (file.processed) {
            val sourceFile = file.path
            val destinationFile = File(outputDir, sourceFile.name)

            try {
                Files.move(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            } catch (e: FileNotFoundException) {
                println("File not found: ${sourceFile.name}.")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}