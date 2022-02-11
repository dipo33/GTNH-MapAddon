package io.github.dipo33.gtmapaddon.mined;

import com.sinthoras.visualprospecting.Utils;
import com.sinthoras.visualprospecting.integration.model.locations.IWaypointAndLocationProvider;
import com.sinthoras.visualprospecting.integration.model.waypoints.Waypoint;
import io.github.dipo33.gtmapaddon.Config;
import net.minecraft.client.resources.I18n;

public class MinedChunkLocation implements IWaypointAndLocationProvider {

    private final int dimensionId;
    private final int chunkX;
    private final int chunkZ;
    private final String minedBy;
    private final int color;

    private boolean isActiveAsWaypoint;

    public MinedChunkLocation(int dimensionId, int chunkX, int chunkZ, String minedBy) {
        this.dimensionId = dimensionId;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.minedBy = minedBy;
        this.color = Config.getColorForUser(minedBy);
    }

    @Override
    public int getDimensionId() {
        return dimensionId;
    }

    public int getIntBlockX() {
        return Utils.coordChunkToBlock(chunkX) + 8;
    }

    @Override
    public double getBlockX() {
        return getIntBlockX() + 0.5;
    }

    public int getIntBlockZ() {
        return Utils.coordChunkToBlock(chunkZ) + 8;
    }

    @Override
    public double getBlockZ() {
        return getIntBlockZ() + 0.5;
    }

    public String getMinedBy() {
        return minedBy;
    }

    public int getColor() {
        return color;
    }

    @Override
    public Waypoint toWaypoint() {
        return new Waypoint(getIntBlockX(), 65, getIntBlockZ(), dimensionId,
                I18n.format("visualprospecting.tracked", String.format("X: %d, Z: %d", chunkX, chunkZ)),
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
        isActiveAsWaypoint = waypoint.dimensionId == dimensionId
                && waypoint.blockX == getIntBlockX()
                && waypoint.blockZ == getIntBlockZ();
    }
}
