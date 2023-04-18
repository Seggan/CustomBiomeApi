package io.github.seggan.custombiomeapi;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Manages {@link CustomBiome}s
 */
public final class CustomBiomeManager {

    private final NmsAccessor accessor;

    private final Map<NamespacedKey, Set<CustomBiome>> biomes = new HashMap<>();

    private final Set<CustomBiome> registeredBiomes = new HashSet<>();

    /**
     * Creates a new {@code CustomBiomeManager} with the provided {@link JavaPlugin}
     * @param plugin the {@code JavaPlugin} creating this manager
     */
    public CustomBiomeManager(@NotNull JavaPlugin plugin) {
        //noinspection SwitchStatementWithTooFewBranches
        this.accessor = switch (Bukkit.getMinecraftVersion()) {
            case "1.19.4" -> new NmsV1_19_4();
            default -> throw new UnsupportedOperationException("Unsupported Minecraft version: " + Bukkit.getMinecraftVersion());
        };

        NamespacedKey replacedKey = new NamespacedKey(plugin, "chunk_biomes_replaced");
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(ignoreCancelled = true)
            void onChunkLoad(ChunkLoadEvent event) {
                Chunk chunk = event.getChunk();
                PersistentDataContainer container = chunk.getPersistentDataContainer();
                if (container.has(replacedKey, PersistentDataType.BYTE)) return;
                container.set(replacedKey, PersistentDataType.BYTE, (byte) 1);
                Set<CustomBiome> biomeList = biomes.get(chunk.getWorld().getKey());
                if (biomeList != null) {
                    accessor.replaceBiomes(chunk, biomeList);
                }
            }
        }, plugin);
    }

    /**
     * Registers a new {@link CustomBiome} with the internal biome registry
     * @param biome the {@code CustomBiome} to register
     */
    public void registerBiome(@NotNull CustomBiome biome) {
        accessor.registerBiome(biome);
        registeredBiomes.add(biome);
    }

    /**
     * Checks if the provided {@link CustomBiome} is registered
     * @param biome the {@code CustomBiome} to check
     * @return whether the {@code CustomBiome} is registered or not
     */
    public boolean isBiomeRegistered(@NotNull CustomBiome biome) {
        return registeredBiomes.contains(biome);
    }

    /**
     * Adds a {@link CustomBiome} to the list of biomes to be applied to a {@link World}. All instances of
     * the {@link CustomBiome#baseBiome()} in the {@code World} will be replaced with the provided {@code CustomBiome}
     * @param world the {@code World} to add the {@code CustomBiome} to
     * @param biome the {@code CustomBiome} to add
     */
    public void addBiomeToWorld(@NotNull World world, @NotNull CustomBiome biome) {
        biomes.computeIfAbsent(world.getKey(), w -> new LinkedHashSet<>()).add(biome);
    }
}
