package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.client.models.ModelAerwhale;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderAerwhale extends Render
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("aether", "textures/mobs/aerwhale/aerwhale.png");
    private ModelBase model = new ModelAerwhale();

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
     */
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        this.func_110777_b(entity);
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(90.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(180.0F - entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return TEXTURE;
    }
}
