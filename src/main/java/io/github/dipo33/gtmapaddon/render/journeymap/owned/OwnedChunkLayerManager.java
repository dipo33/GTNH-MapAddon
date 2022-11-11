package io.github.dipo33.gtmapaddon.render.journeymap.owned;

import com.sinthoras.visualprospecting.integration.model.layers.WaypointProviderManager;
import com.sinthoras.visualprospecting.integration.model.locations.IWaypointAndLocationProvider;
import io.github.dipo33.gtmapaddon.ClientProxy;
import io.github.dipo33.gtmapaddon.storage.owned.OwnedChunk;

import java.util.ArrayList;
import java.util.List;

public class OwnedChunkLayerManager extends WaypointProviderManager {

    public static final OwnedChunkLayerManager instance = new OwnedChunkLayerManager();

    public OwnedChunkLayerManager() {
        super(ClientProxy.OWNED_CHUNKS_BUTTON_MANAGER);
    }

    @Override
    protected List<? extends IWaypointAndLocationProvider> generateVisibleElements(int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ) {
        final List<OwnedChunkLocationProvider> locations = new ArrayList<>();
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(0, 0, 0, "psuchtak", OwnedChunk.Status.BUILD)));
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(1, 0, 0, "DirtyFaced", OwnedChunk.Status.BUILD)));
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(2, 0, 0, "RainDrop_x", OwnedChunk.Status.BUILD)));
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(3, 0, 0, "fb", OwnedChunk.Status.BUILD)));

        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(0, 1, 0, "psuchtak", OwnedChunk.Status.FARM)));
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(1, 1, 0, "DirtyFaced", OwnedChunk.Status.FARM)));
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(2, 1, 0, "RainDrop_x", OwnedChunk.Status.FARM)));
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(3, 1, 0, "fb", OwnedChunk.Status.FARM)));

        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(0, 2, 0, "psuchtak", OwnedChunk.Status.TRANSPORT)));
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(1, 2, 0, "DirtyFaced", OwnedChunk.Status.TRANSPORT)));
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(2, 2, 0, "RainDrop_x", OwnedChunk.Status.TRANSPORT)));
        locations.add(new OwnedChunkLocationProvider(new OwnedChunk(3, 2, 0, "fb", OwnedChunk.Status.TRANSPORT)));

        return locations;
    }

    // TODO: Maybe also override `needsRegenerateVisibleElements`
}
