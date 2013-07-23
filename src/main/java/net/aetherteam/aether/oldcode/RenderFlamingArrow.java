package net.aetherteam.aether.oldcode;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderFlamingArrow extends RenderManager
{
    public void renderArrow(EntityFlamingArrow entityarrow, double d, double d1, double d2, float f, float f1)
    {
        if ((entityarrow.prevRotationYaw == 0.0F) && (entityarrow.prevRotationPitch == 0.0F))
        {
            return;
        }

        a("/aether/other/FlamingArrows.png");
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(entityarrow.prevRotationYaw + (entityarrow.rotationYaw - entityarrow.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entityarrow.prevRotationPitch + (entityarrow.rotationPitch - entityarrow.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
        Rect2i tessellator = Rect2i.rectX;
        int i = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (0 + i * 10) / 32.0F;
        float f5 = (5 + i * 10) / 32.0F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (5 + i * 10) / 32.0F;
        float f9 = (10 + i * 10) / 32.0F;
        float f10 = 0.05625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f11 = entityarrow.arrowShake - f1;

        if (f11 > 0.0F)
        {
            float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
            GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
        }

        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f10, f10, f10);
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);
        tessellator.b();
        tessellator.a(-7.0D, -2.0D, -2.0D, f6, f8);
        tessellator.a(-7.0D, -2.0D, 2.0D, f7, f8);
        tessellator.a(-7.0D, 2.0D, 2.0D, f7, f9);
        tessellator.a(-7.0D, 2.0D, -2.0D, f6, f9);
        tessellator.getRectX();
        GL11.glNormal3f(-f10, 0.0F, 0.0F);
        tessellator.b();
        tessellator.a(-7.0D, 2.0D, -2.0D, f6, f8);
        tessellator.a(-7.0D, 2.0D, 2.0D, f7, f8);
        tessellator.a(-7.0D, -2.0D, 2.0D, f7, f9);
        tessellator.a(-7.0D, -2.0D, -2.0D, f6, f9);
        tessellator.getRectX();

        for (int j = 0; j < 4; j++)
        {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            tessellator.b();
            tessellator.a(-8.0D, -2.0D, 0.0D, f2, f4);
            tessellator.a(8.0D, -2.0D, 0.0D, f3, f4);
            tessellator.a(8.0D, 2.0D, 0.0D, f3, f5);
            tessellator.a(-8.0D, 2.0D, 0.0D, f2, f5);
            tessellator.getRectX();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    public void renderEntityWithPosYaw(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        renderArrow((EntityFlamingArrow)entity, d, d1, d2, f, f1);
    }
}

