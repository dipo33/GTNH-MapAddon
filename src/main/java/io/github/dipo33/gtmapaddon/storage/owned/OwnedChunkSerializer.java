package io.github.dipo33.gtmapaddon.storage.owned;

import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.storage.ChunkStorage;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import io.github.dipo33.gtmapaddon.storage.DimensionStorage;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OwnedChunkSerializer {

    private static class Keys {
        public static final String CHUNK_X = "X";
        public static final String CHUNK_Z = "Z";
        public static final String OWNER = "Owner";
        public static final String STATUS = "Status";
        public static final String DIMENSION_IDS = "DimensionIDs";
        public static final String DIMENSION = "DIM";
    }

    private static NBTTagCompound serialize(DimensionStorage<OwnedChunk> dimensionStorage) {
        NBTTagCompound dataTag = new NBTTagCompound();
        List<Integer> dimIds = new ArrayList<>();
        for (ChunkStorage<OwnedChunk> chunkStorage : dimensionStorage.getAll()) {
            NBTTagList chunksTag = new NBTTagList();
            for (OwnedChunk ownedChunk : chunkStorage.getAll()) {
                NBTTagCompound chunkTag = new NBTTagCompound();
                chunkTag.setInteger(Keys.CHUNK_X, ownedChunk.getChunkX());
                chunkTag.setInteger(Keys.CHUNK_Z, ownedChunk.getChunkZ());
                chunkTag.setInteger(Keys.STATUS, ownedChunk.getStatus().ordinal());
                chunkTag.setString(Keys.OWNER, ownedChunk.getOwner());

                chunksTag.appendTag(chunkTag);
            }

            dataTag.setTag(Keys.DIMENSION + chunkStorage.getDimensionId(), chunksTag);
            dimIds.add(chunkStorage.getDimensionId());
        }

        dataTag.setIntArray(Keys.DIMENSION_IDS, dimIds.stream().mapToInt(Integer::intValue).toArray());
        return dataTag;
    }

    private static DimensionStorage<OwnedChunk> deserialize(NBTTagCompound dataTag) {
        DimensionStorage<OwnedChunk> dimensionStorage = new DimensionStorage<>();
        for (int dimensionId : dataTag.getIntArray(Keys.DIMENSION_IDS)) {
            ChunkStorage<OwnedChunk> chunkStorage = dimensionStorage.getDimension(dimensionId);
            NBTTagList chunksTag = dataTag.getTagList(Keys.DIMENSION + dimensionId, Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < chunksTag.tagCount(); i++) {
                NBTTagCompound chunkTag = chunksTag.getCompoundTagAt(i);

                int chunkX = chunkTag.getInteger(Keys.CHUNK_X);
                int chunkZ = chunkTag.getInteger(Keys.CHUNK_Z);
                String owner = chunkTag.getString(Keys.OWNER);
                OwnedChunk.Status status = OwnedChunk.Status.values()[chunkTag.getInteger(Keys.STATUS)];
                chunkStorage.setElementAtChunk(chunkX, chunkZ, new OwnedChunk(chunkX, chunkZ, dimensionId, owner, status));
            }
        }

        return dimensionStorage;
    }

    public static void save() {
        final File ownedChunksFile = GTMapAddonMod.getCurrentSaveModFile("ownedchunks.dat");

        try {
            CompressedStreamTools.safeWrite(OwnedChunkSerializer.serialize(DataCache.OWNED_CHUNKS_STORAGE), ownedChunksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        final File ownedChunksFile = GTMapAddonMod.getCurrentSaveModFile("ownedchunks.dat");

        try {
            if (ownedChunksFile.exists()) {
                DataCache.OWNED_CHUNKS_STORAGE = deserialize(CompressedStreamTools.read(ownedChunksFile));
            } else {
                DataCache.OWNED_CHUNKS_STORAGE = new DimensionStorage<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
