import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.io.FileNotFoundException

class FilePDF(path: File, wordList: Map<String, Int>?, processed : Boolean) : File_base(path, wordList, processed) {

    init {
        // Execute readFile() when class is instantiated
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

            // Close the document after extracting text
            document.close()

            // Start processing words
            processWords(text)

        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
        } catch (e: FileNotFoundException) {
            println("File not found: ${path.name}. Skipping.")
        }
    }
}