package io.github.dipo33.gtmapaddon.storage;

import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.command.MinedCommand;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MinedChunkSerializer {

    private static NBTTagCompound serialize(DimensionStorage<MinedChunk> dimensionStorage) {
        NBTTagCompound dataTag = new NBTTagCompound();
        List<Integer> dimIds = new ArrayList<>();
        for (ChunkStorage<MinedChunk> chunkStorage : dimensionStorage.getAll()) {
            NBTTagList chunksTag = new NBTTagList();
            for (MinedChunk minedChunk : chunkStorage.getAll()) {
                NBTTagCompound chunkTag = new NBTTagCompound();
                chunkTag.setInteger("X", minedChunk.getChunkX());
                chunkTag.setInteger("Z", minedChunk.getChunkZ());
                chunkTag.setString("By", minedChunk.getMinedBy());

                chunksTag.appendTag(chunkTag);
            }

            dataTag.setTag("DIM" + chunkStorage.getDimensionId(), chunksTag);
            dimIds.add(chunkStorage.getDimensionId());
        }

        dataTag.setIntArray("DimensionIDs", dimIds.stream().mapToInt(Integer::intValue).toArray());
        return dataTag;
    }

    public static void save() {
        final File minedChunksFile = GTMapAddonMod.getCurrentSaveModFile("minedchunks.dat");

        try {
            CompressedStreamTools.safeWrite(MinedChunkSerializer.serialize(MinedCommand.MINED_CHUNKS_STORAGE), minedChunksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
