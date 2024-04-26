import java.io.File

//Reads all files in the input folder and assigns them classes based on their extension
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
    return fileList
}