package io.github.dipo33.gtmapaddon.mined;

import com.sinthoras.visualprospecting.integration.journeymap.drawsteps.ClickableDrawStep;
import com.sinthoras.visualprospecting.integration.journeymap.render.WaypointProviderLayerRenderer;
import com.sinthoras.visualprospecting.integration.model.locations.ILocationProvider;

import java.util.ArrayList;
import java.util.List;

public class MinedChunkLayerRenderer extends WaypointProviderLayerRenderer {

    public static final MinedChunkLayerRenderer instance = new MinedChunkLayerRenderer();

    public MinedChunkLayerRenderer() {
        super(MinedChunkLayerManager.instance);
    }

    @Override
    protected List<? extends ClickableDrawStep> mapLocationProviderToDrawStep(List<? extends ILocationProvider> visibleElements) {
        final List<MinedChunkDrawStep> drawSteps = new ArrayList<>();
        visibleElements.stream()
                .map(element -> (MinedChunkLocation) element)
                .forEach(location -> drawSteps.add(new MinedChunkDrawStep(location)));
        return drawSteps;
    }
}
