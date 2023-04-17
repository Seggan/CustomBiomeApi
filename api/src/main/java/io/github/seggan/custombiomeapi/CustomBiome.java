package io.github.seggan.custombiomeapi;

import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;

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