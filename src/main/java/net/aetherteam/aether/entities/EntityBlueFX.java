package net.aetherteam.aether.entities;

import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;

public class EntityBlueFX extends EntityPortalFX
{
    public EntityBlueFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12)
    {
        super(var1, var2, var4, var6, var8, var10, var12);
        this.particleBlue = 0.9764706F;
        this.particleRed = 0.39215687F;
        this.particleGreen = 0.93333334F;
    }
}
