package net.aetherteam.aether.entities;

import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.world.World;

public class EntityChristmasSnowFX extends EntityPortalFX
{
    public EntityChristmasSnowFX(World world, double d, double d1, double d2, double d3, double d4, double d5)
    {
        super(world, d, d1, d2, d3, d4, d5);
        this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
    }
}
