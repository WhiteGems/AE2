package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import org.lwjgl.opengl.GL11;

public class ModelSlider extends ModelMinecart
{
    public bdi head;

    public ModelSlider()
    {
        this(0.0F);
    }

    public ModelSlider(float f)
    {
        this(f, 0.0F);
    }

    public ModelSlider(float f, float f1)
    {
        this.head = new bdi(this, 0, 0);
        this.head.a(-8.0F, -16.0F, -8.0F, 16, 16, 16, f);
        this.head.a(0.0F, 0.0F + f1, 0.0F);
    }

    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        this.head.a(f5);
        GL11.glPopMatrix();
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.head.g = 0.0F;
        this.head.f = 0.0F;
    }
}

