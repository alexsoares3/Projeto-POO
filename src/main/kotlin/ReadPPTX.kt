import org.apache.poi.sl.extractor.SlideShowExtractor
import org.apache.poi.sl.usermodel.SlideShow
import org.apache.poi.xslf.usermodel.XMLSlideShow
import java.io.FileInputStream

fun main() {
    // Path to the PowerPoint file (.pptx)
    val pptxFilePath = "C:\\Users\\alex.soares3\\ProjetoPOO\\src\\main\\kotlin\\Herança e Polimorfismo(2).pptx"

    // Load the PowerPoint file
    val ppt = XMLSlideShow(FileInputStream(pptxFilePath))

    val text = SlideShowExtractor(ppt)

    println(text.getText())

    // Close the PowerPoint file
    ppt.close()
}
