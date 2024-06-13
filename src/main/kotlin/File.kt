import java.io.File

abstract class File_base(var path: File, var wordList: Map<String, Int>?, var processed : Boolean = false) {
    abstract fun readFile()

    //Splits the text in lines, removes non-word characters, removes spaces and converts to lowercase. Adds and counts words in a Map<Word, NrTimes>
    protected fun processWords(text: String) {

        val wordCountMap = mutableMapOf<String, Int>()

        // Split the text into lines
        val lines = text.split("\n")

        for (line in lines) {
            val words = line.split("[^\\w-]+".toRegex()) // Split by non-word characters

            for (word in words) {
                var sanitizedWord = word.trim().lowercase() // Remove whitespace and convert to lowercase
                if (sanitizedWord.isNotEmpty() && sanitizedWord.length > 2) { // Check if it's not empty and over 2 chars
                    if ("-" in sanitizedWord && sanitizedWord.length <= 4 || sanitizedWord.all { it == '-' }) { // Filter out words that look something like "-los, -nos" or "----"
                        continue
                    }
                    if (sanitizedWord.last().toString() == "-" || sanitizedWord.first().toString() == "-" ) { // Replace - with nothing if it's at the start or end
                        sanitizedWord = sanitizedWord.replace("-", "")                       // For example "Hello-" becomes "Hello"
                    }
                    if (!wordCountMap.containsKey(sanitizedWord)) { // Check if the word is already in the dictionary, if not add it and set its value to 1
                        wordCountMap[sanitizedWord] = 1
                    } else {
                        // Add 1 to existing word
                        wordCountMap[sanitizedWord] = wordCountMap[sanitizedWord]!! + 1
                    }
                }
            }
        }
        wordList = wordCountMap
        processed = true
    }
}