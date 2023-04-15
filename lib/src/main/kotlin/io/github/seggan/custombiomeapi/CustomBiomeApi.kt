package io.github.seggan.custombiomeapi

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.World
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.world.ChunkLoadEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

object CustomBiomeApi {

    private lateinit var plugin: JavaPlugin
    private val accessor: NmsAccessor = when (Bukkit.getMinecraftVersion()) {
        "1.19.4" -> NmsV1_19_4()
        else -> throw IllegalStateException("Unsupported Minecraft version: ${Bukkit.getMinecraftVersion()}")
    }

    private val biomes = mutableSetOf<CustomBiome>()
    private val worlds = mutableMapOf<World, Set<CustomBiome>>()

    fun init(plugin: JavaPlugin) {
        val replacedKey = NamespacedKey(plugin, "chunk_biomes_replaced")
        Bukkit.getPluginManager().registerEvents(object : Listener {
            @EventHandler(ignoreCancelled = true)
            fun onChunkLoad(event: ChunkLoadEvent) {
                val chunk = event.chunk
                if (chunk.persistentDataContainer.has(replacedKey, PersistentDataType.BYTE)) return
                chunk.persistentDataContainer.set(replacedKey, PersistentDataType.BYTE, 1)
                accessor.replaceBiomes(chunk, worlds[chunk.world] ?: return)
            }
        }, plugin)
    }

    fun registerBiome(biome: CustomBiome) {
        accessor.registerBiome(biome)
        biomes.add(biome)
    }

    fun registerWorldForBiomes(world: World, biomes: Set<CustomBiome>) {
        worlds[world] = biomes
    }

    fun unregisterWorldForBiomes(world: World) {
        worlds.remove(world)
    }
}