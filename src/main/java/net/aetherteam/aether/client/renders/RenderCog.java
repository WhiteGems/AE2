package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelCog;
import net.aetherteam.aether.entities.bosses.EntityCog;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderCog extends Render
{
    private ModelCog CogModel = new ModelCog();

    public void Render(EntityCog var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float) var2, (float) var4 + 0.5F, (float) var6);
        GL11.glRotatef(-180.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef((float) (var1.life * 500), 1.0F, 1.0F, 1.0F);

        if (!var1.isLarge())
        {
            GL11.glScalef(0.25F, 0.25F, 0.25F);
        }

        this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/cog.png");
        this.CogModel.render(var1, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        this.Render((EntityCog) var1, var2, var4, var6, var8, var9);
    }
}
