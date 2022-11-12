package io.github.dipo33.gtmapaddon.storage.mined;

import com.sinthoras.visualprospecting.Utils;
import io.github.dipo33.gtmapaddon.storage.serializable.SerializableChunkEntry;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class MinedChunk implements SerializableChunkEntry<MinedChunk> {

    public static final MinedChunk INSTANCE = new MinedChunk(0, 0, 0, null);

    private final int chunkX;
    private final int chunkZ;
    private final int dimensionId;
    private final String minedBy;

    public MinedChunk(int chunkX, int chunkZ, int dimensionId, String minedBy) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.dimensionId = dimensionId;
        this.minedBy = minedBy;
    }

    public int getBlockX() {
        return Utils.coordChunkToBlock(chunkX) + 8;
    }

    public int getBlockZ() {
        return Utils.coordChunkToBlock(chunkZ) + 8;
    }

    public String getMinedBy() {
        return minedBy;
    }

    @Override
    public int getChunkX() {
        return chunkX;
    }

    @Override
    public int getChunkZ() {
        return chunkZ;
    }

    @Override
    public int getDimensionId() {
        return dimensionId;
    }


    private static class Keys {
        public static final String MINED_BY = "By";
    }

    @Override
    public NBTBase serialize(MinedChunk chunk) {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString(Keys.MINED_BY, chunk.getMinedBy());

        return tag;
    }

    @Override
    public MinedChunk deserialize(NBTBase tagBase, int dimensionId, int chunkX, int chunkZ) {
        NBTTagCompound tag = (NBTTagCompound) tagBase;
        String minedBy = tag.getString(Keys.MINED_BY);
        return new MinedChunk(chunkX, chunkZ, dimensionId, minedBy);
    }
}
