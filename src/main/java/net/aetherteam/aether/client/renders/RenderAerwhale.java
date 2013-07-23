package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAerwhale;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class RenderAerwhale extends Render
{
    private ModelBase model = new ModelAerwhale();

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        GL11.glPushMatrix();
        this.loadTexture("/net/aetherteam/aether/client/sprites/mobs/aerwhale/aerwhale.png");
        GL11.glTranslatef((float)var2, (float)var4, (float)var6);
        GL11.glRotatef(90.0F - var1.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F - var1.rotationPitch, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        this.model.render(var1, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}
