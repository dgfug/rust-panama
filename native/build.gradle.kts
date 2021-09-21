plugins {
    id("io.github.arc-blroth.cargo-wrapper") version "1.0.0"
}

buildDir = projectDir.resolve("target")

cargo {
    val libraryFile: String by project
    val cargoProfile: String by project

    outputs = mapOf("" to libraryFile)
    profile = cargoProfile
}

tasks {
    task<Exec>("cbindgen") {
        dependsOn(build)

        val libraryName: String by project
        val libraryHeader: String by project

        val config = file("cbindgen.toml")
        val output = buildDir.resolve(libraryHeader)

        inputs.file(config)
        inputs.dir(workingDir.resolve("src"))
        outputs.file(output)

        commandLine("cbindgen")
        args(
            "--config", config.absolutePath,
            "--crate", libraryName,
            "--output", output.absolutePath
        )
    }
}