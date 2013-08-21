package net.aetherteam.aether.worldgen;

import net.minecraft.world.PortalPosition;

public class AetherPortalPosition extends PortalPosition
{
    /** The worldtime at which this PortalPosition was last verified */
    public long lastUpdateTime;

    /** The teleporter to which this PortalPosition applies */
    final TeleporterAether teleporterInstance;

    public AetherPortalPosition(TeleporterAether par1Teleporter, int par2, int par3, int par4, long par5)
    {
        super(par1Teleporter, par2, par3, par4, par5);
        this.teleporterInstance = par1Teleporter;
        this.lastUpdateTime = par5;
    }
}
