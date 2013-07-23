package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityNotchWave;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderNotchWave extends RenderManager
{
    private float field_77002_a;

    public RenderNotchWave(float par1)
    {
        this.field_77002_a = par1;
    }

    public void doRenderNotchWave(EntityNotchWave notchwave, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float scale = this.field_77002_a;
        GL11.glScalef(scale / 1.0F, scale / 1.0F, scale / 1.0F);
        a("/net/aetherteam/aether/client/sprites/projectiles/notchwave/notchwave.png");
        Rect2i tessellator = Rect2i.rectX;
        float f7 = 1.0F;
        float f8 = 0.5F;
        float f9 = 0.25F;
        GL11.glRotatef(180.0F - this.b.j, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.b.k, 1.0F, 0.0F, 0.0F);
        tessellator.b();
        tessellator.b(0.0F, 1.0F, 0.0F);
        tessellator.a(0.0F - f8, 0.0F - f9, 0.0D, 0.0D, 0.0D);
        tessellator.a(f7 - f8, 0.0F - f9, 0.0D, 0.0D, 1.0D);
        tessellator.a(f7 - f8, 1.0F - f9, 0.0D, 1.0D, 1.0D);
        tessellator.a(0.0F - f8, 1.0F - f9, 0.0D, 1.0D, 0.0D);
        tessellator.getRectX();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    public void renderEntityWithPosYaw(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        doRenderNotchWave((EntityNotchWave)par1Entity, par2, par4, par6, par8, par9);
    }
}

