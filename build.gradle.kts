plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "org.example"
version = "1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.apache.poi:poi-ooxml:5.2.3") //PPTX
    implementation("org.apache.pdfbox:pdfbox:3.0.2") //PDF
    implementation("org.xerial:sqlite-jdbc:3.36.0.3") //SQLite
    implementation("org.apache.poi:poi-ooxml:5.2.4") //DOCX
    implementation("com.github.ajalt.clikt:clikt:4.4.0") //CLI
    implementation(kotlin("stdlib"))
    // Log4j2 Core
    implementation("org.apache.logging.log4j:log4j-core:2.23.1")
    implementation("org.apache.logging.log4j:log4j-api:2.23.1")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
tasks {
    shadowJar {
        archiveBaseName.set("VaultView")
        archiveVersion.set("")
        manifest {
            attributes["Main-Class"] = "org.example.CliKt"
        }
    }
}