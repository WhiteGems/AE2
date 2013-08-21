package net.aetherteam.aether.client.renders;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelThrowingCube extends ModelBase
{
    ModelRenderer cube = new ModelRenderer(this, 0, 0);

    public ModelThrowingCube()
    {
        this.cube.addBox(0.0F, 0.0F, 0.0F, 8, 8, 8);
    }

    public void render(Entity par1Entity, float par7)
    {
        this.cube.render(par7);
    }
}
