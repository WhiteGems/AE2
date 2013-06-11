package net.aetherteam.aether.oldcode;

import net.aetherteam.aether.client.models.ModelBall;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;

public class RenderElectricBall extends RenderLiving
{
    private ModelBall shotty;

    public RenderElectricBall(ModelBase var1, float var2)
    {
        super(var1, var2);
        this.shotty = (ModelBall) var1;
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    public void preRenderCallback(EntityLiving var1, float var2)
    {
        EntityElectricBall var3 = (EntityElectricBall) var1;

        for (int var4 = 0; var4 < 3; ++var4)
        {
            this.shotty.sinage[var4] = var3.sinage[var4];
        }
    }
}
