package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityZephyroo;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;

public class RenderZephyroo extends RenderLiving
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/Zephyroo.png");

    public RenderZephyroo(ModelBase par1ModelBase, float par2)
    {
        super(par1ModelBase, par2);
    }

    public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9)
    {
        super.doRenderLiving(par1EntityLiving, par2, par4, par6, par8, par9);

        if (par1EntityLiving instanceof EntityZephyroo)
        {
            EntityZephyroo roo = (EntityZephyroo)par1EntityLiving;

            if (roo.isLovingClash())
            {
                this.func_96449_a(par1EntityLiving, par2, par4, par6, "请疼爱我, 喔:-O 亲~", par9, 1.0D);
            }
        }
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
