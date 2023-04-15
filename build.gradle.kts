allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
        maven(url = "https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    kotlin("jvm") version "1.8.20"
    id("io.papermc.paperweight.userdev") version "1.5.4" apply false
}

version = "1.0.0"
group = "io.github.seggan"

val versions by extra((properties["versions"] as String).split(','))
val versionProjects by extra(versions.map { ":v" + it.replace('.', '_') })

listOf("lib", "api").forEach {
    project(it) {
        apply(plugin = "org.jetbrains.kotlin.jvm")

        dependencies {
            implementation("org.jetbrains.kotlin:kotlin-stdlib")
        }

        kotlin {
            jvmToolchain(17)
        }
    }
}
