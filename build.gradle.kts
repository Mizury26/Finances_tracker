plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "3.0.1"
}

group = "com.example"
version = "1.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.example.desktopproject")
    mainClass.set("com.example.desktopproject.HelloApplication")
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    implementation("org.xerial:sqlite-jdbc:3.44.1.0")
    implementation("org.slf4j:slf4j-api:2.0.12")
    implementation("org.slf4j:slf4j-simple:2.0.12")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/FinanceTracker-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "Finance Tracker"
    }
    jpackage {
        appVersion = version.toString()

        if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
            imageOptions = listOf(
                "--icon",
                "src/main/resources/images/logoDesktop.ico",
            )
            installerOptions = listOf(
                "--win-dir-chooser",
                "--win-menu",
                "--win-shortcut",
                "--win-per-user-install"
            )
        }

        if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
            imageOptions = listOf(
                "--icon",
                "src/main/resources/images/logoDesktop.icns",
            )
            installerOptions = listOf(
                "--mac-package-name",
                "Finance Tracker",
                "--mac-sign",
                "--mac-app-store",
                "--mac-app-store-provider",
                "YourProviderName"
            )
        }
        if (org.gradle.internal.os.OperatingSystem.current().isLinux) {
            imageOptions = listOf(
                "--icon",
                "src/main/resources/images/logoDesktop.png",
            )
            installerOptions = listOf(
                "--linux-shortcut",
                "--linux-menu-group", "Finance Tracker",
                "--linux-rpm",
            )
        }
    }
}
