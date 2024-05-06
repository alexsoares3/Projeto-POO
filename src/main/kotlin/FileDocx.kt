import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileInputStream
class FileDOCX(path: File, wordList: Map<String, Int>?, processed: Boolean) : File_base(path, wordList, processed) {

    init {
        // Executa readFile() quando a classe é instanciada
        readFile()
    }

    override fun readFile() {
        try {
            FileInputStream(path).use { fis ->
                XWPFDocument(fis).use { document ->
                    val textBuilder = StringBuilder()

                    // Extrai texto de todos os parágrafos
                    document.paragraphs.forEach { paragraph ->
                        textBuilder.append(paragraph.text).append("\n")
                    }
                    // Fechamento do documento é automático com o uso de 'use'

                    val text = textBuilder.toString()

                    // Início do processamento de palavras
                    processWords(text)
                }
            }
        } catch (e: Exception) {
            // Tratamento de exceções
            e.printStackTrace()
        }
    }
}
