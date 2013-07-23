package net.aetherteam.aether.client.renders;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;

public class ModelThrowingCube extends ModelMinecart
{
    bdi cube;

    public ModelThrowingCube()
    {
        this.cube = new bdi(this, 0, 0);
        this.cube.a(0.0F, 0.0F, 0.0F, 8, 8, 8);
    }

    public void render(Entity par1Entity, float par7)
    {
        this.cube.a(par7);
    }
}

