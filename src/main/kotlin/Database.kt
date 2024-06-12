import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.sql.*
import java.util.*

// Start connection to DB
// TODO Add try catch to database connection
fun getConnection(): Connection {
    Class.forName("org.sqlite.JDBC") //Load the JDBC driver class for SQLite
    return DriverManager.getConnection("jdbc:sqlite:database/database.db") //Establish a connection to SQLite database
}

// Create necessary tables to store files/words
// TODO Add try catch to table creation in DB where executeUpdate is used
fun createTables() {
    val connection = getConnection()
    val statement = connection.createStatement()
    statement.executeUpdate("""
        CREATE TABLE IF NOT EXISTS files (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT
        )
    """.trimIndent())

    statement.executeUpdate("""
        CREATE TABLE IF NOT EXISTS file_words (
            file_id INT,
            word TEXT,
            count INT,
            FOREIGN KEY (file_id) REFERENCES files(id)
        )
    """.trimIndent())

    statement.close()
    // Close connection
    connection.close()
}

// Iterates through all files in fileList then iterates through each file's words and if the word has more than 2 chars it gets added to the database.
// File is added first, then a batch with all word counts for this specific file are added, until all files and words are added.
fun insertFilesInDB(fileList: List<File_base>) {
    val connection = getConnection()
    createTables()

    // Prepare statements for SQL query
    val insertFileSql = "INSERT INTO files (name) VALUES (?)"
    val insertWordSql = "INSERT INTO file_words (file_id, word, count) VALUES (?, ?, ?)"

    // Statement.RETURN_GENERATED_KEYS ensures that file ID is returned
    val insertFileStatement = connection.prepareStatement(insertFileSql, Statement.RETURN_GENERATED_KEYS)
    val insertWordStatement = connection.prepareStatement(insertWordSql)


    // Transaction will make everything be inserted at once instead of one by one
    // Changes are first stored in memory and then committed to the database
    // Tested with 250 files and was faster than inserting 50 files previously
    // Start transaction
    // TODO Add try catch from here to when transaction is committed
    connection.autoCommit = false

    for (file in fileList) {
        // Insert file information
        insertFileStatement.setString(1, file.path.name)
        insertFileStatement.executeUpdate()

        // Get the generated file ID
        val fileIdResultSet = insertFileStatement.generatedKeys //Uses the returned IDs
        var fileId: Int? = null
        if (fileIdResultSet.next()) {
            fileId = fileIdResultSet.getInt(1)
        }

        // Insert word counts
        fileId?.let { id ->
            file.wordList?.forEach { (word, count) ->

                insertWordStatement.setInt(1, id)
                insertWordStatement.setString(2, word)
                insertWordStatement.setInt(3, count)
                insertWordStatement.addBatch()

            }
        }
    }

    // Execute batch insert for word counts
    insertWordStatement.executeBatch()

    // Commit transaction
    connection.commit()

    // Close statements
    insertFileStatement.close()
    insertWordStatement.close()

    // Close connection
    connection.close()
}


// Returns all available files and how many words are associated with them
fun getAllFiles(): List<Pair<String, Int>> {
    val connection = getConnection()
    val fileList = mutableListOf<Pair<String, Int>>()

    val sql = """
        SELECT f.name, COUNT(w.word) AS word_count
        FROM files AS f
        LEFT JOIN file_words AS w ON f.id = w.file_id
        GROUP BY f.name """.trimIndent()

    val statement: Statement = connection.createStatement()
    // TODO Add try catch here
    val resultSet: ResultSet = statement.executeQuery(sql)

    while (resultSet.next()) {
        val path = resultSet.getString("name")
        val wordCount = resultSet.getInt("word_count")
        fileList.add(path to wordCount)
    }

    resultSet.close()
    statement.close()
    // Close connection
    connection.close()

    return fileList
}

// This function is used to search for multiple words in the files.
// It takes a string of words as input, splits it into a list of words, and then searches for each word in the files.
fun searchDB(words: String): List<Triple<String, List<String>, Int>> {

    // Return empty list if input is empty
    // Not sure if necessary, since it might be useful to check what files are stored
    if (words.isBlank()) {
        return emptyList()
    }

    // Split the input string into a list of words
    val wordList = words.split(" ")

    // Get a connection to the database
    val connection = getConnection()

    // List to store the search results
    val fileList = mutableListOf<Triple<String, List<String>, Int>>()

    // For each word in the wordList, create a subquery that selects the names of files that contain the word
    val subQueries = wordList.map { word ->
        """
        SELECT f.name, w.word, w.count
        FROM files AS f
        LEFT JOIN file_words AS w ON f.id = w.file_id
        WHERE w.word LIKE '%$word%'
        """.trimIndent()
    }

    // Join the subqueries using union to get the names of files that contain any of the words in the wordList
    val sql = subQueries.joinToString(" UNION ")

    // Prepare the SQL statement
    val statement: PreparedStatement = connection.prepareStatement(sql)

    // TODO Add try catch here
    // Start transaction
    connection.autoCommit = false
    // Execute the SQL query and get the result set
    val resultSet: ResultSet = statement.executeQuery()


    // Store the names of the files that contain all the words (or similar) in the wordList
    val fileWordsMap = mutableMapOf<String, Pair<MutableList<String>, Int>>()

    // Iterate over the result set and add the names of the files and the words to the fileWordsMap
    while (resultSet.next()) {
        // Get the necessary values from the result set
        val path = resultSet.getString("name")
        val word = resultSet.getString("word")
        val count = resultSet.getInt("count")

        // Get the current pair from the fileWordsMap or create a new pair if it doesn't exist
        var currentPair = fileWordsMap.getOrPut(path) { Pair(mutableListOf(), 0) }

        // Add the word to the list of words and update the count
        currentPair.first.add(word)
        currentPair = Pair(currentPair.first, currentPair.second + count)

        // Update the filewordsmap with the new pair
        fileWordsMap[path] = currentPair
    }

    // Close the result set and the statement
    resultSet.close()
    statement.close()
    // Close connection
    connection.close()

    // Iterate over the fileWordsMap and add the file names and the wordList to the fileList
    for ((fileName, pair) in fileWordsMap) {
        // Check if there are the same amount or more of words found and words searched
        if (pair.first.size >= wordList.size) {
            fileList.add(Triple(fileName, pair.first, pair.second))
        }
    }

    // Sort filelist based on word count (highest to lowest)
    fileList.sortByDescending { it.third }

    // Return the fileList
    return fileList
}

// This function resets the database, it also gives the user the option to delete the files in the output directory
// or simply move them back to input folder
fun resetDB() {
    val connection = getConnection()
    val sqlFiles = "DELETE FROM files"
    val sqlFileWords = "DELETE FROM file_words"

    val statementFiles: Statement = connection.createStatement()
    val statementFileWords: Statement = connection.createStatement()

    statementFiles.executeUpdate(sqlFiles)
    statementFileWords.executeUpdate(sqlFileWords)

    statementFiles.close()
    statementFileWords.close()
    connection.close()
    println("Database reset.")

    // Confirm if user wants to delete files
    var answer = ""
    while (answer != "y" && answer != "n") {
        print("Do you want to delete the files? (y/n) ")
        answer = readln().lowercase(Locale.getDefault())
    }
    val directories = createDirectories()
    if (answer == "y") {
        val outputDir = directories["output"]
        var fileCounter = 0
        outputDir?.listFiles()?.forEach { file ->
                try {
                    file.delete()
                    fileCounter++
                } catch (e: FileNotFoundException) {
                    println("File not found: ${file}.")
                }   catch (e: Exception) {
                    e.printStackTrace()
                }
        }
        println("Deleted $fileCounter file(s).")
    } else {
        // Move files back to input directory so they can be processed again.
        val outputDir = directories["output"]
        val inputDir = directories["input"]
        var fileCounter = 0
        outputDir?.listFiles()?.forEach { file ->
            val sourceFile = file.toPath()
            val destinationFile = File(inputDir, sourceFile.fileName.toString())
            try {
                Files.move(sourceFile, destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
                fileCounter++
            } catch (e: FileNotFoundException) {
                println("File not found: ${sourceFile}.")
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        println("Moved $fileCounter file(s) back to input directory.")
    }
}

// TODO Add function wordCloud that takes an int as input and return the x number of words with the most occurences