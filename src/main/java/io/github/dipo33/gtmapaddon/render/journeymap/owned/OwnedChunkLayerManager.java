package io.github.dipo33.gtmapaddon.render.journeymap.owned;

import com.sinthoras.visualprospecting.Utils;
import com.sinthoras.visualprospecting.integration.model.layers.WaypointProviderManager;
import com.sinthoras.visualprospecting.integration.model.locations.IWaypointAndLocationProvider;
import io.github.dipo33.gtmapaddon.ClientProxy;
import io.github.dipo33.gtmapaddon.storage.ChunkStorage;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import io.github.dipo33.gtmapaddon.storage.owned.OwnedChunk;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class OwnedChunkLayerManager extends WaypointProviderManager {

    public static final OwnedChunkLayerManager instance = new OwnedChunkLayerManager();

    public OwnedChunkLayerManager() {
        super(ClientProxy.OWNED_CHUNKS_BUTTON_MANAGER);
    }

    @Override
    protected List<? extends IWaypointAndLocationProvider> generateVisibleElements(int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ) {
        final int minOwnedChunkX = Utils.coordBlockToChunk(minBlockX);
        final int minOwnedChunkZ = Utils.coordBlockToChunk(minBlockZ);
        final int maxOwnedChunkX = Utils.coordBlockToChunk(maxBlockX);
        final int maxOwnedChunkZ = Utils.coordBlockToChunk(maxBlockZ);
        final ChunkStorage<OwnedChunk> chunkStorage = DataCache.OWNED_CHUNKS_STORAGE.getDimension(Minecraft.getMinecraft().thePlayer.dimension);
        
        final List<OwnedChunkLocationProvider> locations = new ArrayList<>();
        for (int chunkX = minOwnedChunkX; chunkX <= maxOwnedChunkX; chunkX++) {
            for (int chunkZ = minOwnedChunkZ; chunkZ <= maxOwnedChunkZ; chunkZ++) {
                final OwnedChunk ownedChunk = chunkStorage.getElementAtChunk(chunkX, chunkZ);
                if (ownedChunk != null) {
                    locations.add(new OwnedChunkLocationProvider(ownedChunk));
                }
            }
        }

        return locations;
    }

    // TODO: Maybe also override `needsRegenerateVisibleElements`
}
