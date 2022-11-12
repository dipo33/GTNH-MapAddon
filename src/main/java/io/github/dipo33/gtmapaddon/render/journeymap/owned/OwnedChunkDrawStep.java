package io.github.dipo33.gtmapaddon.render.journeymap.owned;

import com.sinthoras.visualprospecting.integration.DrawUtils;
import io.github.dipo33.gtmapaddon.Reference;
import io.github.dipo33.gtmapaddon.render.journeymap.ChunkDrawStep;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class OwnedChunkDrawStep extends ChunkDrawStep<OwnedChunkLocationProvider> {

    private static final ResourceLocation PICKAXE_HEAD = new ResourceLocation(Reference.MODID, "textures/pickaxe_head.png");
    private static final ResourceLocation HOE_HEAD = new ResourceLocation(Reference.MODID, "textures/hoe_head.png");
    private static final ResourceLocation TOOL_STICK = new ResourceLocation(Reference.MODID, "textures/tool_stick.png");
    private static final ResourceLocation GEAR = new ResourceLocation(Reference.MODID, "textures/gear.png");

    public OwnedChunkDrawStep(OwnedChunkLocationProvider location) {
        super(location);
    }

    @Override
    public void draw(double chunkSize, double chunkX, double chunkZ) {
        switch (location.getStatus()) {
            case BUILD:
                DrawUtils.drawQuad(TOOL_STICK, chunkX, chunkZ, chunkSize, chunkSize, 0xFFFFFF, 1);
                DrawUtils.drawQuad(PICKAXE_HEAD, chunkX, chunkZ, chunkSize, chunkSize, location.getColor(), 1);
                break;
            case FARM:
                DrawUtils.drawQuad(TOOL_STICK, chunkX, chunkZ, chunkSize, chunkSize, 0xFFFFFF, 1);
                DrawUtils.drawQuad(HOE_HEAD, chunkX, chunkZ, chunkSize, chunkSize, location.getColor(), 1);
                break;
            case TRANSPORT:
                DrawUtils.drawQuad(GEAR, chunkX, chunkZ, chunkSize, chunkSize, location.getColor(), 1);
                break;
        }
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
    }
}
