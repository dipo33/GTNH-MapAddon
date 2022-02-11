package io.github.dipo33.gtmapaddon.mined;

import com.sinthoras.visualprospecting.integration.DrawUtils;
import com.sinthoras.visualprospecting.integration.journeymap.drawsteps.ClickableDrawStep;
import com.sinthoras.visualprospecting.integration.model.locations.IWaypointAndLocationProvider;
import io.github.dipo33.gtmapaddon.Reference;
import journeymap.client.render.map.GridRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MinedChunkDrawStep implements ClickableDrawStep {

    private static final ResourceLocation depletedTextureLocation = new ResourceLocation(Reference.MODID, "textures/depleted.png");

    private final MinedChunkLocation location;

    private double chunkSize;
    private double chunkX;
    private double chunkZ;

    public MinedChunkDrawStep(MinedChunkLocation location) {
        this.location = location;
    }

    @Override
    public void draw(double draggedPixelX, double draggedPixelY, GridRenderer gridRenderer, float drawScale, double fontScale, double rotation) {
        final double blockSize = Math.pow(2, gridRenderer.getZoom());
        final Point2D.Double blockAsPixel = gridRenderer.getBlockPixelInGrid(location.getBlockX(), location.getBlockZ());
        final Point2D.Double pixel = new Point2D.Double(blockAsPixel.getX() + draggedPixelX, blockAsPixel.getY() + draggedPixelY);

        chunkSize = 16 * blockSize;
        final double chunkSizeHalf = chunkSize / 2;
        chunkX = pixel.getX() - chunkSizeHalf;
        chunkZ = pixel.getY() - chunkSizeHalf;

        DrawUtils.drawQuad(depletedTextureLocation, chunkX, chunkZ, chunkSize, chunkSize, location.getColor(), 96);
    }

    @Override
    public List<String> getTooltip() {
        final List<String> tooltip = new ArrayList<>();
        tooltip.add(I18n.format(Reference.MODID + ".mined_by", location.getMinedBy()));

        return tooltip;
    }

    @Override
    public void drawTooltip(FontRenderer fontRenderer, int mouseX, int mouseY, int displayWidth, int displayHeight) {
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= chunkX && mouseX <= chunkX + chunkSize && mouseY >= chunkZ && mouseY <= chunkZ + chunkSize;
    }

    @Override
    public void onActionKeyPressed() {
        // TODO: Maybe do something here ?
    }

    @Override
    public IWaypointAndLocationProvider getLocationProvider() {
        return location;
    }
}
