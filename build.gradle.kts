plugins {
    idea
    java
    application
    id("io.github.krakowski.jextract") version "0.2.3"
}

group = "org.example"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

val crate = "example"
val headerPath = "org.example"
val libName = "$crate.dll"

val nativePath = "native"
val libPath = "$nativePath/target/release/$libName"

repositories {
    mavenCentral()
}

dependencies {

}

tasks {
    task<Exec>("runCbindgen") {
        workingDir = File(nativePath)
        commandLine("cbindgen", "--config", "cbindgen.toml", "--crate", crate, "--output", "lib.h")
    }

    task<Exec>("buildRustLibDebug") {
        workingDir = File(nativePath)
        commandLine("cargo", "build")
    }

    task<Exec>("buildRustLib") {
        workingDir = File(nativePath)
        commandLine("cargo", "build", "--release")
        outputs.file(libPath)
    }

    jextract {
        dependsOn("runCbindgen")
        header("$projectDir/$nativePath/lib.h") {
            libraries.set(listOf("example"))
            targetPackage.set(headerPath)
            className.set("Example")
            sourceMode.set(false)
        }
    }

    withType<JavaExec> {
        jvmArgs = listOf(
            "--add-modules", "jdk.incubator.foreign",
            "--enable-native-access=ALL-UNNAMED",
            "-Djava.library.path=./$nativePath/target/debug"
        )
    }

    withType<JavaCompile> {
        dependsOn("buildRustLibDebug")
    }
}

application {
    mainClass.set("org.example.Main")
    applicationDefaultJvmArgs = listOf(
        "--add-modules", "jdk.incubator.foreign",
        "--enable-native-access=ALL-UNNAMED",
        "-Djava.library.path=../lib"
    )
}

distributions {
    main {
        contents {
            from(tasks["buildRustLib"]) {
                into("lib")
            }
        }
    }
}