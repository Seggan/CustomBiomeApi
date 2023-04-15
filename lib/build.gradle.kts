plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

dependencies {
    @Suppress("UNCHECKED_CAST")
    for (version in rootProject.extra.get("versionProjects") as List<String>) {
        implementation(project(path = version, configuration = "obfuscated"))
    }
    api(project(":api"))
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.shadowJar)
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
        }
    }
}

tasks.shadowJar {
    archiveClassifier.set("")
}