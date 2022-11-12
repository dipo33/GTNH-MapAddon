package io.github.dipo33.gtmapaddon.storage;

import com.sinthoras.visualprospecting.Utils;
import io.github.dipo33.gtmapaddon.storage.serializable.Serializable;
import io.github.dipo33.gtmapaddon.storage.serializable.SerializableChunkEntry;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ChunkStorage<T extends SerializableChunkEntry<T>> implements Serializable<ChunkStorage<T>> {

    private final Map<Integer, Map<Long, T>> dimensions;
    private final T instance;

    public ChunkStorage(T instance) {
        this.dimensions = new ConcurrentHashMap<>();
        this.instance = instance;
    }

    private Map<Long, T> getDimension(int dimensionId) {
        return dimensions.computeIfAbsent(dimensionId, (x) -> new ConcurrentHashMap<>());
    }

    public Collection<Integer> allDimensionIds() {
        return dimensions.keySet();
    }

    public Collection<T> getAll(int dimensionId) {
        return dimensions.computeIfAbsent(dimensionId, (x) -> new ConcurrentHashMap<>()).values();
    }

    public Collection<T> getAll() {
        return dimensions.values().stream()
                    .map(Map::values)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
    }

    public T get(int dimensionId, int chunkX, int chunkZ) {
        Map<Long, T> dimension = getDimension(dimensionId);
        return dimension.get(Utils.chunkCoordsToKey(chunkX, chunkZ));
    }

    public void put(int dimensionId, int chunkX, int chunkZ, T value) {
        Map<Long, T> dimension = getDimension(dimensionId);
        dimension.put(Utils.chunkCoordsToKey(chunkX, chunkZ), value);
    }

    public T remove(int dimensionId, int chunkX, int chunkZ) {
        Map<Long, T> dimension = getDimension(dimensionId);
        return dimension.remove(Utils.chunkCoordsToKey(chunkX, chunkZ));
    }

    private static class Keys {
        public static final String CHUNK_X = "X";
        public static final String CHUNK_Z = "Z";
        public static final String DIMENSION_IDS = "DimensionIDs";
        public static final String DIMENSION = "DIM";
    }

    @Override
    public NBTBase serialize(ChunkStorage<T> storage) {
        NBTTagCompound tag = new NBTTagCompound();
        List<Integer> dimensionIds = new ArrayList<>();

        for (Integer dimensionId : storage.allDimensionIds()) {
            NBTTagList tagChunks = new NBTTagList();

            for (T chunk : storage.getAll(dimensionId)) {
                NBTTagCompound chunkTag = (NBTTagCompound) instance.serialize(chunk);
                chunkTag.setInteger(Keys.CHUNK_X, chunk.getChunkX());
                chunkTag.setInteger(Keys.CHUNK_Z, chunk.getChunkZ());

                tagChunks.appendTag(chunkTag);
            }

            tag.setTag(Keys.DIMENSION + dimensionId, tagChunks);
            dimensionIds.add(dimensionId);
        }

        tag.setIntArray(Keys.DIMENSION_IDS, dimensionIds.stream().mapToInt(Integer::intValue).toArray());
        return tag;
    }

    @Override
    public ChunkStorage<T> deserialize(NBTBase baseTag) {
        NBTTagCompound tag = (NBTTagCompound) baseTag;
        ChunkStorage<T> storage = new ChunkStorage<>(instance);

        for (int dimensionId : tag.getIntArray(Keys.DIMENSION_IDS)) {
            NBTTagList chunksTag = tag.getTagList(Keys.DIMENSION + dimensionId, Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < chunksTag.tagCount(); i++) {
                NBTTagCompound chunkTag = chunksTag.getCompoundTagAt(i);
                int chunkX = chunkTag.getInteger(Keys.CHUNK_X);
                int chunkZ = chunkTag.getInteger(Keys.CHUNK_Z);

                T chunk = instance.deserialize(chunkTag, dimensionId, chunkX, chunkZ);
                storage.put(dimensionId, chunkX, chunkZ, chunk);
            }
        }

        return storage;
    }
}
