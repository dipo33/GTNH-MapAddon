package io.github.dipo33.gtmapaddon.render.journeymap.owned;

import com.sinthoras.visualprospecting.integration.DrawUtils;
import io.github.dipo33.gtmapaddon.Reference;
import io.github.dipo33.gtmapaddon.render.journeymap.ChunkDrawStep;
import io.github.dipo33.gtmapaddon.storage.owned.OwnedChunk;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class OwnedChunkDrawStep extends ChunkDrawStep<OwnedChunkLocationProvider> {

    private static final ResourceLocation BUILD_CHUNK_TEXTURE_LOCATION = new ResourceLocation(Reference.MODID, "textures/build.png");
    private static final ResourceLocation FARM_CHUNK_TEXTURE_LOCATION = new ResourceLocation(Reference.MODID, "textures/farm.png");
    private static final ResourceLocation TRANSPORT_CHUNK_TEXTURE_LOCATION = new ResourceLocation(Reference.MODID, "textures/transport.png");

    public OwnedChunkDrawStep(OwnedChunkLocationProvider location) {
        super(location);
    }

    @Override
    public void draw(double chunkSize, double chunkX, double chunkZ) {
        final ResourceLocation statusTexture = location.getStatus() == OwnedChunk.Status.BUILD ? location.getStatus() == OwnedChunk.Status.FARM ?
                BUILD_CHUNK_TEXTURE_LOCATION :
                FARM_CHUNK_TEXTURE_LOCATION :
                TRANSPORT_CHUNK_TEXTURE_LOCATION;
        DrawUtils.drawQuad(statusTexture, chunkX, chunkZ, chunkSize, chunkSize, location.getColor(), 96);
    }

    @Override
    public List<String> getTooltip() {
        final List<String> tooltip = new ArrayList<>();
        tooltip.add(location.getMainHint());
        tooltip.add(location.getOwnerHint());

        return tooltip;
    }

    @Override
    public void onActionKeyPressed() {
        // TODO
    }
}
