package net.aetherteam.aether.client.renders;

import net.aetherteam.aether.entities.EntityZephyrSnowball;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.Rect2i;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderZephyrSnowball extends RenderManager
{
    public void doRenderFireball(EntityZephyrSnowball entityZephyrSnowball, double d, double d1, double d2, float f, float f1)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        float f2 = 2.0F;
        GL11.glScalef(1.0F, 1.0F, 1.0F);
        Icon icon = Item.snowball.getIconFromDamage(0);
        a("/gui/items.png");
        Rect2i tessellator = Rect2i.rectX;
        float f3 = icon.getMinU();
        float f4 = icon.getMaxU();
        float f5 = icon.getMinV();
        float f6 = icon.getMaxV();
        float f7 = 1.0F;
        float f8 = 0.5F;
        float f9 = 0.25F;
        GL11.glRotatef(180.0F - this.b.j, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.b.k, 1.0F, 0.0F, 0.0F);
        tessellator.b();
        tessellator.b(0.0F, 1.0F, 0.0F);
        tessellator.a(0.0F - f8, 0.0F - f9, 0.0D, f3, f6);
        tessellator.a(f7 - f8, 0.0F - f9, 0.0D, f4, f6);
        tessellator.a(f7 - f8, 1.0F - f9, 0.0D, f4, f5);
        tessellator.a(0.0F - f8, 1.0F - f9, 0.0D, f3, f5);
        tessellator.getRectX();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    public void renderEntityWithPosYaw(Entity entity, double d, double d1, double d2, float f, float f1)
    {
        doRenderFireball((EntityZephyrSnowball)entity, d, d1, d2, f, f1);
    }
}

