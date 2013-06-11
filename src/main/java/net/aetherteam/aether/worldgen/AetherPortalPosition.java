package net.aetherteam.aether.worldgen;

import net.minecraft.world.PortalPosition;

public class AetherPortalPosition extends PortalPosition
{
    /**
     * The worldtime at which this PortalPosition was last verified
     */
    public long lastUpdateTime;

    /**
     * The teleporter to which this PortalPosition applies
     */
    final TeleporterAether teleporterInstance;

    public AetherPortalPosition(TeleporterAether var1, int var2, int var3, int var4, long var5)
    {
        super(var1, var2, var3, var4, var5);
        this.teleporterInstance = var1;
        this.lastUpdateTime = var5;
    }
}
