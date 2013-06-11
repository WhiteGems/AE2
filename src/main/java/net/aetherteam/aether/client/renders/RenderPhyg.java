package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelPhyg2;
import net.aetherteam.aether.entities.mounts.EntityPhyg;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;

public class RenderPhyg extends RenderLiving
{
    private ModelBase wingmodel;

    public RenderPhyg(ModelBase var1, ModelBase var2, float var3)
    {
        super(var1, var3);
        this.setRenderPassModel(var2);
        this.wingmodel = var2;
    }

    protected int setWoolColorAndRender(EntityPhyg var1, int var2, float var3)
    {
        if (var2 == 0)
        {
            this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/phyg/wings.png");
            ModelPhyg2.pig = var1;
            return 1;
        } else
        {
            return -1;
        }
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLiving var1, int var2, float var3)
    {
        return this.setWoolColorAndRender((EntityPhyg) var1, var2, var3);
    }

    private float interpolateRotation(float var1, float var2, float var3)
    {
        float var4;

        for (var4 = var2 - var1; var4 < -180.0F; var4 += 360.0F)
        {
            ;
        }

        while (var4 >= 180.0F)
        {
            var4 -= 360.0F;
        }

        return var1 + var3 * var4;
    }

    public void doRender(EntityLiving var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.doRenderLiving(var1, var2, var4, var6, var8, var9);
    }
}
