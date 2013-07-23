package net.aetherteam.aether.entities;

import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityAetherBreakingFX extends EntityBreakingFX
{
    public EntityAetherBreakingFX(World var1, double var2, double var4, double var6, Item var8)
    {
        super(var1, var2, var4, var6, 0.0D, 0.0D, 0.0D, var8, (RenderEngine)null);
        this.noClip = true;
    }
}
