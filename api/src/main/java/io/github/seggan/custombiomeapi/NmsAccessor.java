package io.github.seggan.custombiomeapi;

import org.bukkit.Chunk;

import java.util.Set;

interface NmsAccessor {

    void registerBiome(CustomBiome biome);

    void replaceBiomes(Chunk chunk, Set<CustomBiome> biomes);
}