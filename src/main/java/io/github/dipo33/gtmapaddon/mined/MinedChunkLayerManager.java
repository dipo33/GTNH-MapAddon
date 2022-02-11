package io.github.dipo33.gtmapaddon.mined;

import com.sinthoras.visualprospecting.integration.model.layers.WaypointProviderManager;
import com.sinthoras.visualprospecting.integration.model.locations.IWaypointAndLocationProvider;
import io.github.dipo33.gtmapaddon.ClientProxy;

import java.util.ArrayList;
import java.util.List;

public class MinedChunkLayerManager extends WaypointProviderManager {

    public static final MinedChunkLayerManager instance = new MinedChunkLayerManager();

    public MinedChunkLayerManager() {
        super(ClientProxy.minedChunksButtonManager);
    }

    @Override
    protected List<? extends IWaypointAndLocationProvider> generateVisibleElements(int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ) {
        // TODO: Change corresponding to OreVeinLayerManager.java
        final List<MinedChunkLocation> locations = new ArrayList<>();
        locations.add(new MinedChunkLocation(0, 0, 0, "psuchtak"));
        locations.add(new MinedChunkLocation(0, 0, 1, "psuchtak"));
        locations.add(new MinedChunkLocation(0, 0, 2, "psuchtak"));
        locations.add(new MinedChunkLocation(0, 0, 3, "psuchtak"));

        locations.add(new MinedChunkLocation(0, 3, -2, "DirtyFaced"));
        locations.add(new MinedChunkLocation(0, 3, -1, "DirtyFaced"));
        locations.add(new MinedChunkLocation(0, 3, 0, "DirtyFaced"));

        locations.add(new MinedChunkLocation(0, -5, -5, "RainDrop_x"));
        locations.add(new MinedChunkLocation(0, -1, 16, "RainDrop_x"));
        locations.add(new MinedChunkLocation(0, 1, 20, "RainDrop_x"));
        locations.add(new MinedChunkLocation(0, 1, 21, "psuchtak"));

        return locations;
    }

    // TODO: Maybe also override `needsRegenerateVisibleElements`
}
