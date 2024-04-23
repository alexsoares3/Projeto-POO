abstract class File(var path : String, var wordList : Map<String,Int>) {
    abstract fun readFile();



}

class FilePDF(path: String, wordList: Map<String, Int>) : File(path,wordList) {
    override fun readFile() {
        TODO("Not yet implemented")
    }

}

class FilePPTX(path: String, wordList: Map<String, Int>) : File(path,wordList) {
    override fun readFile() {
        TODO("Not yet implemented")
    }

}