package io.github.dipo33.gtmapaddon.render.journeymap.mined;

import com.sinthoras.visualprospecting.integration.model.locations.IWaypointAndLocationProvider;
import com.sinthoras.visualprospecting.integration.model.waypoints.Waypoint;
import io.github.dipo33.gtmapaddon.Config;
import io.github.dipo33.gtmapaddon.Reference;
import io.github.dipo33.gtmapaddon.storage.mined.MinedChunk;
import net.minecraft.client.resources.I18n;

public class MinedChunkLocationProvider implements IWaypointAndLocationProvider {

    private final MinedChunk minedChunk;
    private final int color;

    private boolean isActiveAsWaypoint;

    public MinedChunkLocationProvider(MinedChunk minedChunk) {
        this.minedChunk = minedChunk;
        this.color = Config.getColorForUser(minedChunk.getMinedBy());
    }

    @Override
    public int getDimensionId() {
        return minedChunk.getDimensionId();
    }

    @Override
    public double getBlockX() {
        return minedChunk.getBlockX() + 0.5;
    }

    @Override
    public double getBlockZ() {
        return minedChunk.getBlockZ() + 0.5;
    }

    public int getChunkX() {
        return minedChunk.getChunkX();
    }

    public int getChunkZ() {
        return minedChunk.getChunkZ();
    }

    public String getMinedBy() {
        return minedChunk.getMinedBy();
    }

    public int getColor() {
        return color;
    }

    @Override
    public Waypoint toWaypoint() {
        return new Waypoint(minedChunk.getBlockX(), 96, minedChunk.getBlockZ(), minedChunk.getDimensionId(),
                I18n.format(Reference.MODID + ".tracked", minedChunk.getChunkX(), minedChunk.getChunkZ()),
                getColor());
    }

    @Override
    public boolean isActiveAsWaypoint() {
        return isActiveAsWaypoint;
    }

    @Override
    public void onWaypointCleared() {
        isActiveAsWaypoint = false;
    }

    @Override
    public void onWaypointUpdated(Waypoint waypoint) {
        isActiveAsWaypoint = waypoint.dimensionId == minedChunk.getDimensionId()
                && waypoint.blockX == minedChunk.getBlockX()
                && waypoint.blockZ == minedChunk.getBlockZ();
    }

    public String getMainHint() {
        return I18n.format(Reference.MODID + ".mined_by", getMinedBy());
    }
}
