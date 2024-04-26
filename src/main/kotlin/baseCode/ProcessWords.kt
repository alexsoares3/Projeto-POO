import org.apache.poi.sl.draw.geom.Path
import java.io.File

fun processWords(path: String) {

    val wordCountMap = mutableMapOf<String, Int>()

    File(path).forEachLine { line ->

        val words = line.split("\\W+".toRegex()) // Split by non-word characters

        for (word in words) {

            val sanitizedWord = word.trim().lowercase() // Remove whitespace and convert to lowercase

            if (sanitizedWord.isNotEmpty()) { //Check if it's not empty

                if (!wordCountMap.containsKey(sanitizedWord)) { //Check if the word is already in the dictionary, if not add it and set its value to 1

                    wordCountMap[sanitizedWord] = 1

                } else {
                    //Add 1 to existing word
                    wordCountMap[sanitizedWord] = wordCountMap[sanitizedWord]!! + 1
                }
            }
        }
    }

    /*// Print word counts
    wordCountMap.forEach { (word, count) ->
        println("$word: $count")
    }*/
}
