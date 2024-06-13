# VaultView - File management

## Description

This project is a tool for managing and searching through a collection of PDF, PPTX, DOCX and TXT files. 
The application allows you to dump a bunch of files into a specific directory, add the contents of these files to a database, and then search for specific content across all files.

## Features

- **File Processing**: You can place PDF, PPTX, DOCX and TXT files into a specific directory or load them in by providing a folder path.
- **Database Management**: The contents of these files are added to a database, allowing for efficient management and retrieval.
- **Content Search**: The application allows you to search for specific content across all the files.

## Getting Started

### Prerequisites

- JDK 21 or later
- Gradle 8.5 or later

### Installation

Follow these steps to install the application:

1. **Clone the Repository**: Use the following command to clone the repository:
    ```bash
    git clone https://github.com/alexsoares3/Projeto-POO.git
    ```

2. **Navigate to the Project Directory**: After cloning the repository, navigate to the project directory using the following command:
    ```bash
    cd Projeto-POO
    ```

3. **Build the Project**: Finally, build the project using Gradle with the following command:
    ```bash
    gradlew shadowJar
    ```
   A jar file should now be available in `build/libs`.


## Usage

- Run the application using `java -jar VaultView.jar`. This will give you a list of available commands and create the necessary folders for you.



## Available commands
- `-h` or `--help` for a list of commands
- `list` Lists all files in the database.
- `search` Searches the database for files with matching words. Example: search word1 word2
- `load` Allows you to load files from a directory of your choice and copies the processed files to the output directory.
- `reset` Resets the database, gives you the option to delete all files in the output directory or copy them back to input directory.
- All commands besides `load` and `reset` will load files from `filesToRead` folder when ran.
#### Example:
- `java -jar VaultView.jar list`
- `java -jar VaultView.jar load C:\Users\testFolder`
- `java -jar VaultView.jar search some words`
- `java -jar VaultView.jar reset`

    
