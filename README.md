# CustomBiomeApi

This is a simple API for creating custom biomes in Spigot/Paper plugins.

## Usage

### Gradle (Kotlin DSL)
```kotlin
repositories {
    ...
    maven(url = "https://jitpack.io")
}

dependencies {
    ...
    implementation("com.github.seggan:CustomBiomeApi:[VERSION]")
}
```

### Gradle (Groovy DSL)
```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}

dependencies {
    ...
    implementation 'com.github.seggan:CustomBiomeApi:[VERSION]'
}
```

### Maven
```xml
<repositories>
    ...
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
<dependencies>
    ...
    <dependency>
        <groupId>com.github.seggan</groupId>
        <artifactId>CustomBiomeApi</artifactId>
        <version>[VERSION]</version>
    </dependency>
</dependencies>
```

### Example
```java
CustomBiome biome = ...;
CustomBiomeManager manager = new CustomBiomeManager(plugin);
manager.registerBiome(biome);
manager.addBiomeToWorld(world, biome);
```

## Versions

| Version | Changelog                                 |
|---------|-------------------------------------------|
| 1.0.0   | Initial release. Support for 1.19.4 only. |