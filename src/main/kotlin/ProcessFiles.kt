import java.io.File

import moveFiles

//Reads all files in the input folder and assigns them classes based on their extension
// processFiles() > readFile() from all objects > processWords() from all objects > moveFiles()
// Every time a File is instantiated the File.readFile() runs, then File.processWords() runs. Files are then moved to a different folder.
fun processFiles(): MutableList<File_base> {
    val inputDir = File("filesToRead") //Input directory
    val fileList: MutableList<File_base> = mutableListOf()

    //iterate through all files in dir
    inputDir.listFiles()?.forEach { file ->

        if (file.isFile) { //verify if normal file

            if (file.extension.equals("pdf", ignoreCase = true)) {
                fileList += FilePDF(file, null)

            } else if (file.extension.equals("pptx", ignoreCase = true)) {
                fileList += FilePPTX(file, null)
            }
        }
    }
    moveFiles()
    return fileList
}