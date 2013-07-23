package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityZephyroo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLiving;

public class RenderZephyroo extends RenderLiving
{
    public RenderZephyroo(ModelBase var1, float var2)
    {
        super(var1, var2);
    }

    public void doRenderLiving(EntityLiving var1, double var2, double var4, double var6, float var8, float var9)
    {
        super.doRenderLiving(var1, var2, var4, var6, var8, var9);

        if (var1 instanceof EntityZephyroo)
        {
            EntityZephyroo var10 = (EntityZephyroo)var1;

            if (var10.isLovingClash())
            {
                this.func_96449_a(var1, var2, var4, var6, "请疼爱我, 喔:-O 亲~", var9, 1.0D);
            }
        }
    }
}
