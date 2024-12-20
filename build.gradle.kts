plugins {
    id("java-library")
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "com.datasiqn"
version = "1.0.0"

repositories {
    mavenCentral()

    maven {
        name = "WPILib"
        setUrl("https://frcmaven.wpi.edu/artifactory/release")
    }
}

dependencies {
    compileOnly("edu.wpi.first.shuffleboard:api:2024.3.2")
}

javafx {
    version = "21.0.2"
    modules("javafx.fxml")
}

tasks.register<Copy>("installPlugin") {
    from(tasks.named("jar"))
    into("${System.getProperty("user.home")}/Shuffleboard/plugins")
    description = "Builds the plugin JAR and installs it in the Shuffleboard plugins directory."
    group = "Shuffleboard Plugin"
}
