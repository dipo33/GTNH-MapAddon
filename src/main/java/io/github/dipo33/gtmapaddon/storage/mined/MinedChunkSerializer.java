package io.github.dipo33.gtmapaddon.storage.mined;

import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.command.MinedCommand;
import io.github.dipo33.gtmapaddon.storage.ChunkStorage;
import io.github.dipo33.gtmapaddon.storage.DimensionStorage;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MinedChunkSerializer {

    private static class Keys {
        public static final String CHUNK_X = "X";
        public static final String CHUNK_Z = "Z";
        public static final String MINED_BY = "By";
        public static final String DIMENSION_IDS = "DimensionIDs";
        public static final String DIMENSION = "DIM";
    }

    private static NBTTagCompound serialize(DimensionStorage<MinedChunk> dimensionStorage) {
        NBTTagCompound dataTag = new NBTTagCompound();
        List<Integer> dimIds = new ArrayList<>();
        for (ChunkStorage<MinedChunk> chunkStorage : dimensionStorage.getAll()) {
            NBTTagList chunksTag = new NBTTagList();
            for (MinedChunk minedChunk : chunkStorage.getAll()) {
                NBTTagCompound chunkTag = new NBTTagCompound();
                chunkTag.setInteger(Keys.CHUNK_X, minedChunk.getChunkX());
                chunkTag.setInteger(Keys.CHUNK_Z, minedChunk.getChunkZ());
                chunkTag.setString(Keys.MINED_BY, minedChunk.getMinedBy());

                chunksTag.appendTag(chunkTag);
            }

            dataTag.setTag(Keys.DIMENSION + chunkStorage.getDimensionId(), chunksTag);
            dimIds.add(chunkStorage.getDimensionId());
        }

        dataTag.setIntArray(Keys.DIMENSION_IDS, dimIds.stream().mapToInt(Integer::intValue).toArray());
        return dataTag;
    }

    private static DimensionStorage<MinedChunk> deserialize(NBTTagCompound dataTag) {
        DimensionStorage<MinedChunk> dimensionStorage = new DimensionStorage<>();
        for (int dimensionId : dataTag.getIntArray(Keys.DIMENSION_IDS)) {
            ChunkStorage<MinedChunk> chunkStorage = dimensionStorage.getDimension(dimensionId);
            NBTTagList chunksTag = dataTag.getTagList(Keys.DIMENSION + dimensionId, Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < chunksTag.tagCount(); i++) {
                NBTTagCompound chunkTag = chunksTag.getCompoundTagAt(i);

                int chunkX = chunkTag.getInteger(Keys.CHUNK_X);
                int chunkZ = chunkTag.getInteger(Keys.CHUNK_Z);
                String minedBy = chunkTag.getString(Keys.MINED_BY);
                chunkStorage.setElementAtChunk(chunkX, chunkZ, new MinedChunk(chunkX, chunkZ, dimensionId, minedBy));
            }
        }

        return dimensionStorage;
    }

    public static void save() {
        final File minedChunksFile = GTMapAddonMod.getCurrentSaveModFile("minedchunks.dat");

        try {
            CompressedStreamTools.safeWrite(MinedChunkSerializer.serialize(MinedCommand.MINED_CHUNKS_STORAGE), minedChunksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read() {
        final File mineChunksFile = GTMapAddonMod.getCurrentSaveModFile("minedchunks.dat");

        try {
            if (mineChunksFile.exists()) {
                MinedCommand.MINED_CHUNKS_STORAGE = deserialize(CompressedStreamTools.read(mineChunksFile));
            } else {
                MinedCommand.MINED_CHUNKS_STORAGE = new DimensionStorage<>();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
