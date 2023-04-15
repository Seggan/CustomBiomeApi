package io.github.seggan.custombiomeapi

import org.bukkit.Color
import org.bukkit.NamespacedKey
import org.bukkit.block.Biome

data class CustomBiome(
    val key: NamespacedKey,
    val baseBiome: Biome,
    val isRainy: Boolean,
    val fogColor: Color,
    val waterColor: Color,
    val waterFogColor: Color,
    val skyColor: Color,
    val grassColor: Color,
    val foliageColor: Color
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CustomBiome) return false
        return key == other.key
    }

    override fun hashCode(): Int = key.hashCode()
}