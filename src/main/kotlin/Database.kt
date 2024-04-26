import java.sql.*

// Quando tem muitos ficheiros o envio para a db demora um bocado (optimizar fun insertFilesInDB()?)
// Adicionar procura por varias palavras?

// Start connection to DB
fun getConnection(): Connection {
    Class.forName("org.sqlite.JDBC") //Load the JDBC driver class for SQLite
    return DriverManager.getConnection("jdbc:sqlite:database/database.db") //Establish a connection to SQLite database
}

// Create necessary tables to store files/words
fun createTables() {
    val connection = getConnection()
    val statement = connection.createStatement()
    statement.executeUpdate("""
        CREATE TABLE IF NOT EXISTS files (
            id INT PRIMARY KEY,
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
                if (count > 2) {
                    insertWordStatement.setInt(1, id)
                    insertWordStatement.setString(2, word)
                    insertWordStatement.setInt(3, count)
                    insertWordStatement.addBatch()
                }
            }
        }
    }

    // Execute batch insert for word counts
    insertWordStatement.executeBatch()

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
    val resultSet: ResultSet = statement.executeQuery(sql)

    while (resultSet.next()) {
        val path = resultSet.getString("name")
        val wordCount = resultSet.getInt("word_count")
        fileList.add(path to wordCount)
    }

    resultSet.close()
    statement.close()

    return fileList
}

// Searches for words similar to the word provided. Returns list with files it was found in and the word that matches with the search word
fun searchSimilarWords(word: String): List<Triple<Int, String, String>> {
    // List to store similar words found in files
    val similarWordsFiles = mutableListOf<Triple<Int, String, String>>()

    // Obtain a database connection
    val connection = getConnection()

    // SQL query to search for similar words in files
    val sql = """
        SELECT f.id, f.name, w.word
        FROM files f
        INNER JOIN file_words w ON f.id = w.file_id
        WHERE w.word LIKE ?
        GROUP BY f.id, f.name, w.word
        ORDER BY LENGTH(w.word) ASC
    """.trimIndent()

    // Prepare SQL statement with parameter for the word
    val statement: PreparedStatement = connection.prepareStatement(sql)
    statement.setString(1, "%$word%")

    // Execute SQL query
    val resultSet: ResultSet = statement.executeQuery()

    // Iterate over the result set
    while (resultSet.next()) {
        // Extract file ID, file name, and similar word found
        val fileId = resultSet.getInt("id")
        val fileName = resultSet.getString("name")
        val foundWord = resultSet.getString("word")

        // Add the extracted information to the list
        similarWordsFiles.add(Triple(fileId, fileName, foundWord))
    }

    // Close result set and statement
    resultSet.close()
    statement.close()

    // Return the list of similar words found in files
    return similarWordsFiles
}