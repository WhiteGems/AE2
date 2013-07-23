package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityLightningKnife;
import net.aetherteam.aether.items.AetherItems;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderLightningKnife extends RenderManager
{
    public void renderEntityWithPosYaw(Entity var1, double var2, double var4, double var6, float var8, float var9)
    {
        doRenderKnife((EntityLightningKnife)var1, var2, var4, var6, var8, var9);
    }

    public void doRenderKnife(EntityLightningKnife arr, double d, double d1, double d2, float yaw, float time)
    {
        String texture = "/gui/items.png";
        Icon icon = AetherItems.LightningKnife.getIconFromDamage(0);
        float texMinX = icon.getMinU();
        float texMaxX = icon.getMaxU();
        float texMinY = icon.getMinV();
        float texMaxY = icon.getMaxV();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-(arr.prevRotationPitch + (arr.rotationPitch - arr.prevRotationPitch) * time), 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        a(texture);
        Rect2i tessellator = Rect2i.rectX;
        float f4 = 1.0F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f8 = 0.0625F;
        GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
        tessellator.b();
        tessellator.b(0.0F, 0.0F, 1.0F);
        tessellator.a(0.0D, 0.0D, 0.0D, texMaxX, texMaxY);
        tessellator.a(f4, 0.0D, 0.0D, texMinX, texMaxY);
        tessellator.a(f4, 0.0D, 1.0D, texMinX, texMinY);
        tessellator.a(0.0D, 0.0D, 1.0D, texMaxX, texMinY);
        tessellator.getRectX();
        tessellator.b();
        tessellator.b(0.0F, 0.0F, -1.0F);
        tessellator.a(0.0D, 0.0F - f8, 1.0D, texMaxX, texMinY);
        tessellator.a(f4, 0.0F - f8, 1.0D, texMinX, texMinY);
        tessellator.a(f4, 0.0F - f8, 0.0D, texMinX, texMaxY);
        tessellator.a(0.0D, 0.0F - f8, 0.0D, texMaxX, texMaxY);
        tessellator.getRectX();
        tessellator.b();
        tessellator.b(-1.0F, 0.0F, 0.0F);

        for (int i = 0; i < 16; i++)
        {
            float f9 = i / 16.0F;
            float f13 = texMaxX + (texMinX - texMaxX) * f9 - 0.00195313F;
            float f17 = f4 * f9;
            tessellator.a(f17, 0.0F - f8, 0.0D, f13, texMaxY);
            tessellator.a(f17, 0.0D, 0.0D, f13, texMaxY);
            tessellator.a(f17, 0.0D, 1.0D, f13, texMinY);
            tessellator.a(f17, 0.0F - f8, 1.0D, f13, texMinY);
        }

        tessellator.getRectX();
        tessellator.b();
        tessellator.b(1.0F, 0.0F, 0.0F);

        for (int j = 0; j < 16; j++)
        {
            float f10 = j / 16.0F;
            float f14 = texMaxX + (texMinX - texMaxX) * f10 - 0.00195313F;
            float f18 = f4 * f10 + 0.0625F;
            tessellator.a(f18, 0.0F - f8, 1.0D, f14, texMinY);
            tessellator.a(f18, 0.0D, 1.0D, f14, texMinY);
            tessellator.a(f18, 0.0D, 0.0D, f14, texMaxY);
            tessellator.a(f18, 0.0F - f8, 0.0D, f14, texMaxY);
        }

        tessellator.getRectX();
        tessellator.b();
        tessellator.b(0.0F, 1.0F, 0.0F);

        for (int k = 0; k < 16; k++)
        {
            float f11 = k / 16.0F;
            float f15 = texMaxY + (texMinY - texMaxY) * f11 - 0.00195313F;
            float f19 = f4 * f11 + 0.0625F;
            tessellator.a(0.0D, 0.0D, f19, texMaxX, f15);
            tessellator.a(f4, 0.0D, f19, texMinX, f15);
            tessellator.a(f4, 0.0F - f8, f19, texMinX, f15);
            tessellator.a(0.0D, 0.0F - f8, f19, texMaxX, f15);
        }

        tessellator.getRectX();
        tessellator.b();
        tessellator.b(0.0F, -1.0F, 0.0F);

        for (int l = 0; l < 16; l++)
        {
            float f12 = l / 16.0F;
            float f16 = texMaxY + (texMinY - texMaxY) * f12 - 0.00195313F;
            float f20 = f4 * f12;
            tessellator.a(f4, 0.0D, f20, texMinX, f16);
            tessellator.a(0.0D, 0.0D, f20, texMaxX, f16);
            tessellator.a(0.0D, 0.0F - f8, f20, texMaxX, f16);
            tessellator.a(f4, 0.0F - f8, f20, texMinX, f16);
        }

        tessellator.getRectX();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}

