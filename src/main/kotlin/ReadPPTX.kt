import org.apache.poi.sl.extractor.SlideShowExtractor
import org.apache.poi.xslf.usermodel.XMLSlideShow
import java.io.File
import java.io.FileInputStream

fun readPPXT() {
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

    inputDir.listFiles()?.forEach { file ->
        if (file.isFile && file.extension.equals("pptx", ignoreCase = true)) {
            // Load the PowerPoint file
            val document = XMLSlideShow(FileInputStream(file))
            try {
                // Extract text from the slideshows
                val text = SlideShowExtractor(document).getText()

                // Construct output file path
                val outputFilePath = File(outputDir, "${file.nameWithoutExtension}.txt")

                // Write extracted text to file
                outputFilePath.writeText(text)
                println("Text extracted and saved to: ${outputFilePath.path}")

            } finally {
                // Close the PowerPoint file
                document.close()

                counter += 1
                println("$counter/$nrOfFiles")
            }
        }
    }

    println("Extracted text from $counter out of $nrOfFiles files.")
}
