package io.github.seggan.custombiomeapi

import org.bukkit.Chunk

interface NmsAccessor {

    fun registerBiome(biome: CustomBiome)

    fun replaceBiomes(chunk: Chunk, biomes: Set<CustomBiome>)
}