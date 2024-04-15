plugins {
    kotlin("jvm") version "1.9.22"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.apache.poi:poi:5.2.3") //PPTX
    implementation("org.apache.poi:poi-ooxml:5.2.3") //PPTX
    implementation("org.apache.pdfbox:pdfbox:3.0.2") //PDF
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}