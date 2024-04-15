import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File

/*fun readPdf(fileToRead : String) {
    // Path to the PDF file
    val pdfFilePath = "filesToRead/$fileToRead"

    // Load the PDF document using the companion object
    val document = Loader.loadPDF(File(pdfFilePath))

    try {
        // Create PDFTextStripper object
        val pdfStripper = PDFTextStripper()

        // Extract text from the PDF
        val text = pdfStripper.getText(document)

        // Print the extracted text
        println(text)

        // If you want to store the text in a file
        val outputFilePath = "extractedText/Class_1 - Kotlin basics.txt"
        File(outputFilePath).writeText(text)
        println("Text extracted and saved to: $outputFilePath")
    } finally {
        // Close the document
        document.close()
    }
}*/

fun readPdf(){

    val inputDir = File("filesToRead")
    val outputDir = File("extractedText")

    // Check if input directory exists and is a directory
    if (!inputDir.exists() || !inputDir.isDirectory) {
        println("Folder not found.")
        inputDir.mkdirs()
    }


    // Check if output directory exists or create it if it doesn't
    if (!outputDir.exists() || !outputDir.isDirectory) {
        outputDir.mkdirs()
    }

    var counter = 0
    val nrOfFiles = inputDir.listFiles()?.size

    //iterate through all files in dir
    inputDir.listFiles()?.forEach { file ->
        if (file.isFile && file.extension.equals("pdf", ignoreCase = true)) { //verify if normal file and if pdf
            // Load the PDF document

            val document = Loader.loadPDF(file)

            try {

                // Create PDFTextStripper object
                val pdfStripper = PDFTextStripper()

                // Extract text from the PDF
                val text = pdfStripper.getText(document)

                // Construct output file path
                val outputFilePath = File(outputDir, "${file.nameWithoutExtension}.txt")

                // Write extracted text to file
                outputFilePath.writeText(text)

                println("Text extracted and saved to: ${outputFilePath.path}")
            } finally {
                // Close the document
                document.close()
                counter += 1
                println("$counter/$nrOfFiles")
            }
        }
    }

    println("Extracted text from $counter out of $nrOfFiles files.")
}
