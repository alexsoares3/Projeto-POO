import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File

class FilePDF(path: File, wordList: Map<String, Int>?) : File_base(path, wordList) {

    init {
        readFile()
    }

    override fun readFile() {

        // Load the PDF document
        val document = Loader.loadPDF(path)

        try {
            // Create PDFTextStripper object
            val pdfStripper = PDFTextStripper()

            // Extract text from the PDF
            val text = pdfStripper.getText(document)

            // Start processing words
            processWords(text)

        } finally {
            // Close the document
            document.close()
        }
    }
}