package io.github.dipo33.gtmapaddon.render.journeymap;

import com.sinthoras.visualprospecting.integration.journeymap.drawsteps.ClickableDrawStep;
import com.sinthoras.visualprospecting.integration.model.locations.IWaypointAndLocationProvider;
import journeymap.client.render.map.GridRenderer;
import net.minecraft.client.gui.FontRenderer;

import java.awt.geom.Point2D;

public abstract class ChunkDrawStep<T extends IWaypointAndLocationProvider> implements ClickableDrawStep {

    protected final T location;

    private double chunkSize;
    private double chunkX;
    private double chunkZ;

    public ChunkDrawStep(T location) {
        this.location = location;
    }

    @Override
    public final void draw(double draggedPixelX, double draggedPixelY, GridRenderer gridRenderer, float drawScale, double fontScale, double rotation) {
        final double blockSize = Math.pow(2, gridRenderer.getZoom());
        final Point2D.Double blockAsPixel = gridRenderer.getBlockPixelInGrid(location.getBlockX(), location.getBlockZ());
        final Point2D.Double pixel = new Point2D.Double(blockAsPixel.getX() + draggedPixelX, blockAsPixel.getY() + draggedPixelY);

        chunkSize = 16 * blockSize;
        final double chunkSizeHalf = chunkSize / 2;
        chunkX = pixel.getX() - chunkSizeHalf;
        chunkZ = pixel.getY() - chunkSizeHalf;

        draw(chunkSize, chunkX, chunkZ);
    }

    public abstract void draw(double chunkSize, double chunkX, double chunkZ);

    @Override
    public void drawTooltip(FontRenderer fontRenderer, int mouseX, int mouseY, int displayWidth, int displayHeight) {
    }

    @Override
    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= chunkX && mouseX <= chunkX + chunkSize && mouseY >= chunkZ && mouseY <= chunkZ + chunkSize;
    }

    @Override
    public IWaypointAndLocationProvider getLocationProvider() {
        return location;
    }
}
