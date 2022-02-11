package io.github.dipo33.gtmapaddon.storage;

import com.sinthoras.visualprospecting.Utils;

import java.util.HashMap;
import java.util.Map;

public class ChunkStorage<T> {

    private final int dimensionId;

    private final Map<Long, T> chunks;

    public ChunkStorage(int dimensionId) {
        this.dimensionId = dimensionId;
        chunks = new HashMap<>();
    }

    public T getElementAtChunk(int chunkX, int chunkZ) {
        return chunks.get(Utils.chunkCoordsToKey(chunkX, chunkZ));
    }

    public void setElementAtChunk(int chunkX, int chunkZ, T element) {
        chunks.put(Utils.chunkCoordsToKey(chunkX, chunkZ), element);
    }

    public int getDimensionId() {
        return dimensionId;
    }
}
