package io.github.dipo33.gtmapaddon.storage;

import io.github.dipo33.gtmapaddon.data.entity.MinedChunk;
import io.github.dipo33.gtmapaddon.data.entity.OwnedChunk;
import io.github.dipo33.gtmapaddon.data.entity.PrizeList;
import io.github.dipo33.gtmapaddon.storage.serializable.Serializer;

public class DataCache {

    public static ChunkStorage<MinedChunk> MINED_CHUNKS_STORAGE;
    public static ChunkStorage<OwnedChunk> OWNED_CHUNKS_STORAGE;
    public static PrizeList PRIZE_LIST;

    public static final Serializer<ChunkStorage<MinedChunk>> MINED_CHUNKS_SERIALIZER = new Serializer<>(
                "minedchunks", () -> new ChunkStorage<>(MinedChunk.INSTANCE),
                () -> MINED_CHUNKS_STORAGE, (item) -> MINED_CHUNKS_STORAGE = item
    );
    public static final Serializer<ChunkStorage<OwnedChunk>> OWNED_CHUNKS_SERIALIZER = new Serializer<>(
                "ownedchunks", () -> new ChunkStorage<>(OwnedChunk.INSTANCE),
                () -> OWNED_CHUNKS_STORAGE, (item) -> OWNED_CHUNKS_STORAGE = item
    );
    public static final Serializer<PrizeList> PRIZE_LIST_SERIALIZER = new Serializer<>(
                "prizelist", PrizeList::new,
                () -> PRIZE_LIST, (item) -> PRIZE_LIST = item
    );
}
