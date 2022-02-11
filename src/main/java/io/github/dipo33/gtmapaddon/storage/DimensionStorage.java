package io.github.dipo33.gtmapaddon.storage;

import java.util.HashMap;
import java.util.Map;

public class DimensionStorage<T> {

    private final Map<Integer, ChunkStorage<T>> dimensions;

    public DimensionStorage() {
        dimensions = new HashMap<>();
    }

    public ChunkStorage<T> getDimension(int dimensionId) {
        if (!dimensions.containsKey(dimensionId)) {
            dimensions.put(dimensionId, new ChunkStorage<>(dimensionId));
        }

        return dimensions.get(dimensionId);
    }
}
