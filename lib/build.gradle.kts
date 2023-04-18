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

tasks.register<Jar>("javadocJar") {
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

tasks.shadowJar {
    archiveClassifier.set("")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(tasks.shadowJar)
            artifact(tasks.named("javadocJar"))
            groupId = rootProject.group.toString()
            artifactId = rootProject.name
            version = rootProject.version.toString()
        }
    }
}