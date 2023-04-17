package io.github.seggan.custombiomeapi;

import com.google.common.collect.Maps;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_19_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_19_R3.CraftServer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

class NmsV1_19_4 implements NmsAccessor {

    private static final Map<org.bukkit.block.Biome, ResourceLocation> biomeToKey = Arrays.stream(org.bukkit.block.Biome.values())
        .collect(Collectors.toMap(
            Function.identity(),
            biome -> nsKeyToResourceLocation(biome.getKey())
        ));

    private final Map<CustomBiome, ResourceKey<Biome>> customBiomeToKey = Maps.newHashMap();
    private final MappedRegistry<Biome> biomeRegistry = (MappedRegistry<Biome>) ((CraftServer) Bukkit.getServer()).getServer().registryAccess().registryOrThrow(Registries.BIOME);

    public void registerBiome(@NotNull CustomBiome biome) {
        Biome.BiomeBuilder builder = new Biome.BiomeBuilder();
        builder.generationSettings(BiomeGenerationSettings.EMPTY);
        builder.mobSpawnSettings(MobSpawnSettings.EMPTY);
        builder.temperatureAdjustment(Biome.TemperatureModifier.NONE);
        builder.hasPrecipitation(biome.isRainy());

        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder();
        effects.fogColor(biome.fogColor().asRGB());
        effects.waterColor(biome.waterColor().asRGB());
        effects.waterFogColor(biome.waterFogColor().asRGB());
        effects.skyColor(biome.skyColor().asRGB());
        effects.grassColorOverride(biome.grassColor().asRGB());
        effects.foliageColorOverride(biome.foliageColor().asRGB());
        effects.grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE);
        builder.specialEffects(effects.build());

        ResourceKey<Biome> key = ResourceKey.create(
            Registries.BIOME,
            nsKeyToResourceLocation(biome.key())
        );
        customBiomeToKey.put(biome, key);

        unfreezeRegistry(biomeRegistry);
        biomeRegistry.register(key, builder.build(), Lifecycle.stable());
        biomeRegistry.freeze();
    }

    @Override
    public void replaceBiomes(@NotNull Chunk chunk, @NotNull Set<CustomBiome> biomes) {
        CraftChunk craftChunk = (CraftChunk) chunk;
        ChunkAccess nmsChunk = craftChunk.getHandle(ChunkStatus.FULL);
        int maxY = chunk.getWorld().getMaxHeight();
        for (int x = 0; x < 16; x++) {
            for (int y = chunk.getWorld().getMinHeight(); y < maxY; y++) {
                for (int z = 0; z < 16; z++) {
                    Holder<Biome> biomeHolder = nmsChunk.getNoiseBiome(x, y, z);
                    for (CustomBiome customBiome : biomes) {
                        if (biomeHolder.is(biomeToKey.get(customBiome.baseBiome()))) {
                            nmsChunk.setBiome(x, y, z, biomeRegistry.getHolderOrThrow(customBiomeToKey.get(customBiome)));
                            break;
                        }
                    }
                }
            }
        }
        nmsChunk.setUnsaved(true);
        craftChunk.getCraftWorld().getHandle().getChunkSource().chunkMap.resendBiomesForChunks(List.of(nmsChunk));
    }

    private static void unfreezeRegistry(MappedRegistry<?> registry) {
        try {
            for (Field field : MappedRegistry.class.getDeclaredFields()) {
                if (field.getType() == boolean.class) {
                    field.setAccessible(true);
                    field.set(registry, false);
                }
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private static ResourceLocation nsKeyToResourceLocation(NamespacedKey key) {
        return new ResourceLocation(key.getNamespace(), key.getKey());
    }
}
