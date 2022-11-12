package io.github.dipo33.gtmapaddon.render.journeymap.mined;

import com.sinthoras.visualprospecting.integration.DrawUtils;
import io.github.dipo33.gtmapaddon.GTMapAddonMod;
import io.github.dipo33.gtmapaddon.Reference;
import io.github.dipo33.gtmapaddon.network.RemoveMinedChunkServerMessage;
import io.github.dipo33.gtmapaddon.render.journeymap.ChunkDrawStep;
import io.github.dipo33.gtmapaddon.storage.DataCache;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class MinedChunkDrawStep extends ChunkDrawStep<MinedChunkLocationProvider> {

    private static final ResourceLocation DEPLETED_TEXTURE_LOCATION = new ResourceLocation(Reference.MODID, "textures/depleted.png");

    public MinedChunkDrawStep(MinedChunkLocationProvider location) {
        super(location);
    }

    @Override
    public void draw(double chunkSize, double chunkX, double chunkZ) {
        DrawUtils.drawQuad(DEPLETED_TEXTURE_LOCATION, chunkX, chunkZ, chunkSize, chunkSize, location.getColor(), 96);
    }

    @Override
    public List<String> getTooltip() {
        final List<String> tooltip = new ArrayList<>();
        tooltip.add(location.getMainHint());

        return tooltip;
    }

    @Override
    public void onActionKeyPressed() {
        DataCache.MINED_CHUNKS_STORAGE.remove(location.getDimensionId(), location.getChunkX(), location.getChunkZ());
        GTMapAddonMod.NETWORK_CHANNEL.sendToServer(new RemoveMinedChunkServerMessage(location));
    }
}
