package net.aetherteam.aether.entities;

import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;

public class EntityAetherPortalFX extends EntityPortalFX
{
    public EntityAetherPortalFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
    {
        super(world, d, d1, d2, d3, d4, d5);
        float f = this.rand.nextFloat() * 0.6F + 0.4F;
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F * f;
        this.particleRed *= 0.2F;
        this.particleGreen *= 0.2F;
        this.noClip = true;
    }
}
