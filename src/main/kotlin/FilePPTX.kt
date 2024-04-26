import org.apache.poi.sl.extractor.SlideShowExtractor
import org.apache.poi.xslf.usermodel.XMLSlideShow
import java.io.File
import java.io.FileInputStream

class FilePPTX(path: File, wordList: Map<String, Int>?) : File_base(path, wordList) {

    init {
        readFile()
    }

    override fun readFile() {
        val document = XMLSlideShow(FileInputStream(path))
        try {
            // Extract text from the slideshows
            val text = SlideShowExtractor(document).getText()
            processWords(text)

        } finally {
            // Close the PowerPoint file
            document.close()
        }
    }

}