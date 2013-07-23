package net.aetherteam.aether.entities;

import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;

public class EntityPurpleFX extends EntityPortalFX
{
    public EntityPurpleFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12)
    {
        super(var1, var2, var4, var6, var8, var10, var12);
        this.particleRed = 0.4F;
        this.particleGreen = 0.2F;
        this.particleBlue = 0.6F;
    }
}
