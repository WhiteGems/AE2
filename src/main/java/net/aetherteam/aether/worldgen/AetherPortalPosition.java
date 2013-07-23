package net.aetherteam.aether.worldgen;

import net.minecraft.world.PortalPosition;

public class AetherPortalPosition extends PortalPosition
{
    public long field_85087_d;
    final TeleporterAether field_85088_e;

    public AetherPortalPosition(TeleporterAether par1Teleporter, int par2, int par3, int par4, long par5)
    {
        super(par1Teleporter, par2, par3, par4, par5);
        this.field_85088_e = par1Teleporter;
        this.lastUpdateTime = par5;
    }
}

