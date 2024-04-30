import org.apache.poi.xwpf.usermodel.XWPFDocument
import java.io.File
import java.io.FileInputStream

// Definição da classe base, adaptar conforme necessário
open class File_base(val path: File, var wordList: Map<String, Int>?, var processed: Boolean) {
    open fun readFile() {
        // Implementação de leitura básica, sobrescrita nas subclasses
    }

    protected fun processWords(text: String) {
        // Implementar a lógica para processar palavras
        println("Processing words...")
    }
}

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

                    // Início do processamento de palavras
                    processWords(textBuilder.toString())
                }
            }
        } catch (e: Exception) {
            // Tratamento de exceções
            e.printStackTrace()
        }
    }
}

fun main() {
    val filePath = File("caminho/para/seu/arquivo.docx")
    val docxFile = FileDOCX(filePath, null, false)
}
