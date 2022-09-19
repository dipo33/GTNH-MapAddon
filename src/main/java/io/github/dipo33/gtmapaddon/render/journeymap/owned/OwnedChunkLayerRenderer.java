package io.github.dipo33.gtmapaddon.render.journeymap.owned;

import com.sinthoras.visualprospecting.integration.journeymap.drawsteps.ClickableDrawStep;
import com.sinthoras.visualprospecting.integration.journeymap.render.WaypointProviderLayerRenderer;
import com.sinthoras.visualprospecting.integration.model.locations.ILocationProvider;
import io.github.dipo33.gtmapaddon.render.journeymap.mined.MinedChunkDrawStep;
import io.github.dipo33.gtmapaddon.render.journeymap.mined.MinedChunkLayerManager;
import io.github.dipo33.gtmapaddon.render.journeymap.mined.MinedChunkLocationProvider;

import java.util.ArrayList;
import java.util.List;

public class OwnedChunkLayerRenderer extends WaypointProviderLayerRenderer {

    public static final OwnedChunkLayerRenderer instance = new OwnedChunkLayerRenderer();

    public OwnedChunkLayerRenderer() {
        super(OwnedChunkLayerManager.instance);
    }

    @Override
    protected List<? extends ClickableDrawStep> mapLocationProviderToDrawStep(List<? extends ILocationProvider> visibleElements) {
        final List<OwnedChunkDrawStep> drawSteps = new ArrayList<>();
        visibleElements.stream()
                .map(element -> (OwnedChunkLocationProvider) element)
                .forEach(location -> drawSteps.add(new OwnedChunkDrawStep(location)));
        return drawSteps;
    }
}
