package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelCog;
import net.aetherteam.aether.entities.bosses.EntityCog;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderCog extends Render
{
    public static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/cog.png");
    private ModelCog CogModel = new ModelCog();

    public void Render(EntityCog cog, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4 + 0.5F, (float)par6);
        GL11.glRotatef(-180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef((float)(cog.life * 500), 1.0F, 1.0F, 1.0F);

        if (!cog.isLarge())
        {
            GL11.glScalef(0.25F, 0.25F, 0.25F);
        }

        this.func_110777_b(cog);
        this.CogModel.render(cog, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.Render((EntityCog)par1Entity, par2, par4, par6, par8, par9);
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
