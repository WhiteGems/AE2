package net.aetherteam.aether.entities;

import net.minecraft.client.particle.EntityBreakingFX;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityAetherBreakingFX extends EntityBreakingFX
{
    public EntityAetherBreakingFX(World par1World, double par2, double par4, double par6, Item par8Item)
    {
        super(par1World, par2, par4, par6, par8Item);
        this.noClip = true;
    }
}
