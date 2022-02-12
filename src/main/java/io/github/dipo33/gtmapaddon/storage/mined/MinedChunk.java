package io.github.dipo33.gtmapaddon.storage.mined;

import com.sinthoras.visualprospecting.Utils;

public class MinedChunk {

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

    public int getChunkX() {
        return chunkX;
    }

    public int getBlockX() {
        return Utils.coordChunkToBlock(chunkX) + 8;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public int getBlockZ() {
        return Utils.coordChunkToBlock(chunkZ) + 8;
    }

    public int getDimensionId() {
        return dimensionId;
    }

    public String getMinedBy() {
        return minedBy;
    }
}
