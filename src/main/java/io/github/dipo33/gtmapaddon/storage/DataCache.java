package io.github.dipo33.gtmapaddon.storage;

import io.github.dipo33.gtmapaddon.storage.mined.MinedChunk;
import io.github.dipo33.gtmapaddon.storage.owned.OwnedChunk;

public class DataCache {

    public static DimensionStorage<MinedChunk> MINED_CHUNKS_STORAGE;
    public static DimensionStorage<OwnedChunk> OWNED_CHUNKS_STORAGE;
}
