package net.aetherteam.aether.entities;

import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityAetherBreakingFX extends ServerList
{
    public EntityAetherBreakingFX(World par1World, double par2, double par4, double par6, Item par8Item)
    {
        super(par1World, par2, par4, par6, 0.0D, 0.0D, 0.0D, par8Item, null);
        this.Z = true;
    }
}

