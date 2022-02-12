package io.github.dipo33.gtmapaddon.render.journeymap.mined;

import com.sinthoras.visualprospecting.Utils;
import com.sinthoras.visualprospecting.integration.model.layers.WaypointProviderManager;
import com.sinthoras.visualprospecting.integration.model.locations.IWaypointAndLocationProvider;
import io.github.dipo33.gtmapaddon.ClientProxy;
import io.github.dipo33.gtmapaddon.command.MinedCommand;
import io.github.dipo33.gtmapaddon.storage.ChunkStorage;
import io.github.dipo33.gtmapaddon.storage.mined.MinedChunk;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class MinedChunkLayerManager extends WaypointProviderManager {

    public static final MinedChunkLayerManager instance = new MinedChunkLayerManager();

    public MinedChunkLayerManager() {
        super(ClientProxy.minedChunksButtonManager);
    }

    @Override
    protected List<? extends IWaypointAndLocationProvider> generateVisibleElements(int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ) {
        final int minMinedChunkX = Utils.coordBlockToChunk(minBlockX);
        final int minMinedChunkZ = Utils.coordBlockToChunk(minBlockZ);
        final int maxMinedChunkX = Utils.coordBlockToChunk(maxBlockX);
        final int maxMinedChunkZ = Utils.coordBlockToChunk(maxBlockZ);
        final ChunkStorage<MinedChunk> chunkStorage = MinedCommand.MINED_CHUNKS_STORAGE.getDimension(Minecraft.getMinecraft().thePlayer.dimension);

        final List<MinedChunkLocationProvider> locations = new ArrayList<>();
        for (int chunkX = minMinedChunkX; chunkX <= maxMinedChunkX; chunkX++) {
            for (int chunkZ = minMinedChunkZ; chunkZ <= maxMinedChunkZ; chunkZ++) {
                final MinedChunk minedChunk = chunkStorage.getElementAtChunk(chunkX, chunkZ);
                if (minedChunk != null) {
                    locations.add(new MinedChunkLocationProvider(minedChunk));
                }
            }
        }

        return locations;
    }

    // TODO: Maybe also override `needsRegenerateVisibleElements`
}
