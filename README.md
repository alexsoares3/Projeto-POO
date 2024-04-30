# File management

## Description

This project is a powerful tool for managing and searching through a collection of PDF and PPTX files. It is particularly useful for students, researchers, and anyone who needs to manage and search through a large number of documents. The project is built using Kotlin, Java, and Gradle.

## Features

- **File Dumping**: You can dump a bunch of PDF and PPTX files into a specific directory.
- **Database Management**: The contents of these files are added to a database, allowing for efficient management and retrieval.
- **Content Search**: The application allows you to search for specific content across all the dumped files.

## Getting Started

### Prerequisites

- JDK 11 or later
- Gradle 7.0 or later

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
    gradle build
    ```


## Usage

1. Dump your PDF and PPTX files into the `filesToRead` directory.
2. Run the application. This will add the contents of the files to the database.
3. Use the `search` function to search for specific content. The function takes a string of words as input, splits it into a list of words, and then searches for each word in the files.

