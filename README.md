# rust-panama

A template project for setting up both Rust and the Java 18 Panama Foreign Linker API *(via jextract).*

### How to Setup

* Clone the repo.
* Modify the values in `gradle.properties` to your liking.
* You're done!

## Why Rust?

**Rust** has limited support in the Java ecosystem, and as such, deserves to have some attention (especially since it's a very nice *and* fast language). Due to the difficulty of interfacing with a native application like Cargo, the template uses a Gradle plugin called `gradle-cargo-wrapper` that runs Cargo via Gradle's [`Exec`](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.Exec.html) task type.

**Read more about Rust:**
* [rust-lang.org](https://www.rust-lang.org)
* [The Book](https://doc.rust-lang.org/book)

## What's Panama?

[**Project Panama**](https://openjdk.java.net/projects/panama) (shortened to just **Panama**) is an upcoming collection of Java features designed to enhance performance safely.<br>
Currently, Panama includes 3 major APIs:

- Memory Access ([JEP-383](https://openjdk.java.net/jeps/383))
- Foreign Linker (FFI/Foreign Function Interface) ([JEP-389](https://openjdk.java.net/jeps/389))
- Vector ([JEP-338](https://openjdk.java.net/jeps/338))

These all work with low-level concepts, like memory, and are *very* efficient.<br>
The main API we are looking at for this template is the **Foreign Linker,** as it allows for a high-speed alternative to JNI (Java Native Interface), which is what allows us to interface with functions in native languages, like C or Rust.

The Foreign Linker is quite complicated by itself, but thankfully a tool was created to automatically generate the required bindings for a C header file. This tool is called `jextract` and is used by this template.

## Special Thanks

* @krakowski - [gradle-jextract](https://github.com/krakowski/gradle-jextract)
* @Arc-blroth - [gradle-cargo-wrapper](https://github.com/Arc-blroth/gradle-cargo-wrapper)
