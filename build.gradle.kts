plugins {
    id("io.papermc.paperweight.userdev") version "1.5.4" apply false
    `java-library`
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://repo.papermc.io/repository/maven-public/")
    }

    apply(plugin = "java-library")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

version = "1.0.0"
group = "io.github.seggan"

val versions by extra((properties["versions"] as String).split(','))
val versionProjects by extra(versions.map { ":v" + it.replace('.', '_') })
