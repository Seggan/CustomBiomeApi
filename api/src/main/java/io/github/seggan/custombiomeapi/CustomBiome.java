package io.github.seggan.custombiomeapi;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a custom biome
 * @param key the {@link NamespacedKey} of the biome
 * @param baseBiome the base biome of the biome. This is the biome that will be replaced in the world
 * @param isRainy whether the biome is rainy
 * @param fogColor the color of the fog in the biome
 * @param waterColor the color of the water in the biome
 * @param waterFogColor the color of the water fog in the biome
 * @param skyColor the color of the sky in the biome
 * @param grassColor the color of the grass in the biome
 * @param foliageColor the color of the foliage in the biome
 */
public record CustomBiome(
    @NotNull NamespacedKey key,
    @NotNull Biome baseBiome,
    @NotNull Boolean isRainy,
    @NotNull Color fogColor,
    @NotNull Color waterColor,
    @NotNull Color waterFogColor,
    @NotNull Color skyColor,
    @NotNull Color grassColor,
    @NotNull Color foliageColor
) {

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CustomBiome biome && key.equals(biome.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}