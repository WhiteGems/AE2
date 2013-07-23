package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

public class ModelTempest extends ModelMinecart
{
    bdi main_body;
    bdi RB_cloud;
    bdi LB_cloud;
    bdi tail_2;
    bdi FR_cloud;
    bdi FL_cloud;
    bdi tail_1;
    public float sinage;
    public float sinage2;

    public ModelTempest()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.main_body = new bdi(this, 0, 0);
        this.main_body.a(-6.0F, -4.0F, -7.0F, 12, 8, 14);
        this.main_body.a(0.0F, 10.0F, 0.0F);
        this.main_body.b(64, 64);
        this.main_body.i = true;
        setRotation(this.main_body, 0.0F, 0.0F, 0.0F);
        this.RB_cloud = new bdi(this, 16, 22);
        this.RB_cloud.a(-8.0F, -2.0F, 0.0F, 2, 6, 6);
        this.RB_cloud.a(0.0F, 10.0F, 0.0F);
        this.RB_cloud.b(64, 64);
        this.RB_cloud.i = true;
        setRotation(this.RB_cloud, 0.0F, 0.0F, 0.0F);
        this.LB_cloud = new bdi(this, 16, 22);
        this.LB_cloud.a(6.0F, -2.0F, 0.0F, 2, 6, 6);
        this.LB_cloud.a(0.0F, 10.0F, 0.0F);
        this.LB_cloud.b(64, 64);
        this.LB_cloud.i = true;
        setRotation(this.LB_cloud, 0.0F, 0.0F, 0.0F);
        this.LB_cloud.i = false;
        this.tail_2 = new bdi(this, 32, 22);
        this.tail_2.a(-2.0F, -2.0F, -2.0F, 4, 4, 4);
        this.tail_2.a(0.0F, 10.0F, 19.0F);
        this.tail_2.b(64, 64);
        this.tail_2.i = true;
        setRotation(this.tail_2, 0.0F, 0.0F, 0.0F);
        this.FR_cloud = new bdi(this, 0, 22);
        this.FR_cloud.a(-8.0F, -3.0F, -7.0F, 2, 6, 6);
        this.FR_cloud.a(0.0F, 10.0F, 0.0F);
        this.FR_cloud.b(64, 64);
        this.FR_cloud.i = true;
        setRotation(this.FR_cloud, 0.0F, 0.0F, 0.0F);
        this.FL_cloud = new bdi(this, 0, 22);
        this.FL_cloud.a(6.0F, -3.0F, -7.0F, 2, 6, 6);
        this.FL_cloud.a(0.0F, 10.0F, 0.0F);
        this.FL_cloud.b(64, 64);
        this.FL_cloud.i = true;
        setRotation(this.FL_cloud, 0.0F, 0.0F, 0.0F);
        this.FL_cloud.i = false;
        this.tail_1 = new bdi(this, 0, 34);
        this.tail_1.a(-3.0F, -3.0F, -3.0F, 6, 6, 6);
        this.tail_1.a(0.0F, 10.0F, 12.0F);
        this.tail_1.b(64, 64);
        this.tail_1.i = true;
        setRotation(this.tail_1, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.main_body.a(f5);
        this.RB_cloud.a(f5);
        this.LB_cloud.a(f5);
        this.FR_cloud.a(f5);
        this.FL_cloud.a(f5);
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        this.tail_1.a(f5);
        this.tail_2.a(f5);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public void renderTransparentTail(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.tail_1.a(f5);
        this.tail_2.a(f5);
    }

    private void setRotation(bdi model, float x, float y, float z)
    {
        model.f = x;
        model.g = y;
        model.h = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        float boff = this.sinage2;
        float yOffset = 7.5F;
        float vertMotion = (float)(Math.sin(f * 20.0F / (180D / Math.PI)) * this.sinage * 0.5D);
        float PI = (float)Math.PI;
        float initialOffset = PI / 2.0F;
        float offset = PI * 3.0F / 11.0F;
        this.FR_cloud.d = (vertMotion + 10.0F);
        this.FL_cloud.c = (vertMotion * 0.5F);
        this.LB_cloud.d = (8.0F - vertMotion * 0.5F);
        this.RB_cloud.d = (9.0F + vertMotion * 0.5F);
        this.tail_1.c = ((float)(Math.sin(f * 20.0F / (180D / Math.PI)) * f1 * 0.75D));
        this.tail_1.g = ((float)Math.pow(0.9900000095367432D, -4.0D) * 1.0F * PI / 4.0F * MathHelper.cos(-0.055F * f + initialOffset));
        this.tail_1.d = (10.0F - vertMotion);
        this.tail_2.c = ((float)Math.pow(0.9900000095367432D, 1.0D) * 1.0F * PI / 4.0F * MathHelper.cos(-0.055F * f + initialOffset));
        this.tail_2.d = (10.0F - vertMotion * 1.25F);
        this.tail_2.g = (this.tail_1.g + 0.25F);
        this.main_body.d = (boff + yOffset + this.sinage * 2.0F);
        this.RB_cloud.d = (boff + yOffset + this.sinage * 2.0F);
        this.LB_cloud.d = (boff + yOffset + this.sinage * 2.0F);
        this.tail_2.d = (boff + yOffset + this.sinage * 2.0F);
        this.FR_cloud.d = (boff + yOffset + this.sinage * 2.0F);
        this.FL_cloud.d = (boff + yOffset + this.sinage * 2.0F);
        this.tail_1.d = (boff + yOffset + this.sinage * 2.0F);
    }
}

