import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.StandardCopyOption

//Reads all files in the input folder and assigns them classes based on their extension
// processFiles() > readFile() from all objects > processWords() from all objects > moveFiles() > insertFilesInDB()
// Every time a File is instantiated the File.readFile() runs, then File.processWords() runs. Files are then moved to a different folder.
// Finally files are added to the database in tables files(id, fileName) and file_words(file_id,word,count)
fun processFiles() {
    val directories = createDirectories()
    val inputDir = directories["input"] //Input directory
    val outputDir = directories["output"] //Output directory
    val fileList: MutableList<File_base> = mutableListOf()

    val outputDirList = outputDir?.listFiles()?.map { it.name } ?: emptyList()

    val skipTracker = mutableListOf<String>()

    if (!inputDir?.listFiles()?.isEmpty()!!) {
        println("Loading files...")
    }

    //iterate through all files in dir
    inputDir.listFiles()?.forEach { file ->

        if (file.isFile && !outputDirList.contains(file.name)) { //verify if normal file and if file is already processed
            if (file.extension.equals("pdf", ignoreCase = true)) {
                fileList += FilePDF(file, null, false)

            } else if (file.extension.equals("pptx", ignoreCase = true)) {
                fileList += FilePPTX(file, null, false)
            } else if (file.extension.equals("docx", ignoreCase = true)) {
                fileList += FileDOCX(file, null, false)
            } else if (file.extension.equals("txt", ignoreCase = true)) {
                fileList += FileDOCX(file, null, false)
            }
        } else {
            skipTracker += file.name
        }

    }
    if (skipTracker.isNotEmpty()) {
        println("Skipped ${skipTracker.size} file(s).")
    }
    // Move processed files to output directory
    moveFiles(fileList)

    // Insert processed files in database
    insertFilesInDB(fileList)

    if (fileList.isNotEmpty()) {
        println("Loaded ${fileList.size} file(s).")
    }

}

fun moveFiles(processedFiles: List<File_base>) {
    val outputDir = createDirectories()["output"]

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

fun createDirectories(): Map<String, File> { //Create necessary directories (on startup)
    val inputDir = File("filesToRead")
    val outputDir = File("processedFiles")
    val databaseDir = File("database")
    val directories = mapOf("input" to inputDir, "output" to outputDir, "database" to databaseDir)


    // Check if input directory exists or create it if it doesn't
    if (!inputDir.exists() || !inputDir.isDirectory) {
        inputDir.mkdirs()
    }

    // Check if output directory exists or create it if it doesn't
    if (!outputDir.exists() || !outputDir.isDirectory) {
        outputDir.mkdirs()
    }

    // Check if database directory exists or create it if it doesn't
    if (!databaseDir.exists() || !databaseDir.isDirectory) {
        databaseDir.mkdirs()
    }

    return directories
}