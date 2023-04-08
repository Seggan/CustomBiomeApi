allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.20"
    id("io.papermc.paperweight.userdev") version "1.5.4" apply false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
    }

    kotlin {
        jvmToolchain(17)
    }
}
