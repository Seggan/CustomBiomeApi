package io.github.seggan.custombiomeapi;

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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class CustomBiomeManager {

    private final NmsAccessor accessor;

    private final Map<World, Set<CustomBiome>> biomes = new HashMap<>();

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
                Set<CustomBiome> biomeList = biomes.get(chunk.getWorld());
                if (biomeList != null) {
                    accessor.replaceBiomes(chunk, biomeList);
                }
            }
        }, plugin);
    }

    public void registerBiome(@NotNull CustomBiome biome) {
        accessor.registerBiome(biome);
    }

    public void addBiomeToWorld(@NotNull World world, @NotNull CustomBiome biome) {
        biomes.computeIfAbsent(world, w -> new LinkedHashSet<>()).add(biome);
    }
}
