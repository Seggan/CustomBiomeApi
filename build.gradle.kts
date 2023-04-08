allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.20" apply false
    id("io.papermc.paperweight.userdev") version "1.5.4" apply false
}
