plugins {
    idea
    java
    application
    id("io.github.krakowski.jextract") version "0.2.3"
}

group = "org.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Put your dependencies here.
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

val libraryName: String by project
val libraryFile: String by project
val libraryHeader: String by project

val outputPackage: String by project
val outputClass: String by project

val cargoProfile: String by project

tasks {
    jextract {
        dependsOn(":native:cbindgen")
        header("$projectDir/native/target/$libraryHeader") {
            libraries.set(listOf(libraryName))
            targetPackage.set(outputPackage)
            className.set(outputClass)
            sourceMode.set(false)
        }
    }

    withType<JavaExec> {
        jvmArgs = listOf(
            "--add-modules", "jdk.incubator.foreign",
            "--enable-native-access=ALL-UNNAMED",
            "-Djava.library.path=./native/target/$cargoProfile"
        )
    }
}

application {
    val mainClass: String by project
    this.mainClass.set(mainClass)
    applicationDefaultJvmArgs = listOf(
        "--add-modules", "jdk.incubator.foreign",
        "--enable-native-access=ALL-UNNAMED",
        "-Djava.library.path=../lib"
    )
}

distributions {
    main {
        contents {
            from(tasks.getByPath(":native:build")) {
                into("lib")
            }
        }
    }
}