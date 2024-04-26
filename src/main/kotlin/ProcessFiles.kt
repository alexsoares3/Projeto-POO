import java.io.File

import moveFiles

//Reads all files in the input folder and assigns them classes based on their extension
// processFiles() > readFile() from all objects > processWords() from all objects > moveFiles()
// Every time a File is instantiated the File.readFile() runs, then File.processWords() runs. Files are then moved to a different folder.
fun processFiles(): MutableList<File_base> {
    val inputDir = File("filesToRead") //Input directory
    val outputDir = File("processedFiles") //Input directory
    val fileList: MutableList<File_base> = mutableListOf()

    val outputDirList = outputDir.listFiles()?.map { it.name } ?: emptyList()

    val skipTracker = mutableListOf<String>()

    //iterate through all files in dir
    inputDir.listFiles()?.forEach { file ->

        if (file.isFile && !outputDirList.contains(file.name)) { //verify if normal file and if file is already processed
            if (file.extension.equals("pdf", ignoreCase = true)) {
                fileList += FilePDF(file, null)

            } else if (file.extension.equals("pptx", ignoreCase = true)) {
                fileList += FilePPTX(file, null)
            }
        } else {
            skipTracker += file.name
        }

    }
    if (skipTracker.isNotEmpty()) {
        println("Skipped ${skipTracker.size} files.")
    }
    moveFiles(skipTracker)
    return fileList
}