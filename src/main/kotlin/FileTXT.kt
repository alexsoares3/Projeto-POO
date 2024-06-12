import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import java.io.File
import java.io.FileNotFoundException

class FileTXT(path: File, wordList: Map<String, Int>?, processed : Boolean) : File_base(path, wordList, processed) {

    init {
        // Execute readFile() when class is instantiated
        readFile()
    }

    override fun readFile() {

        // Load the TXT document
        val document = path.toString()

        try {
           // Extrair o texto do Documento
            val text = File(document).readText()

            // Start processing words
            processWords(text)

        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
        }
    }
}