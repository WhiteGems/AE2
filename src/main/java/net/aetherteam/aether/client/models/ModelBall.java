package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import org.lwjgl.opengl.GL11;

public class ModelBall extends ModelMinecart
{
    public bdi[] head;
    public float[] sinage;
    private static final float sponge = (180F / (float)Math.PI);

    public ModelBall()
    {
        this(0.0F);
    }

    public ModelBall(float f)
    {
        this(f, 0.0F);
    }

    public ModelBall(float f, float f1)
    {
        this.sinage = new float[3];
        this.head = new bdi[3];
        this.head[0] = new bdi(this, 0, 0);
        this.head[1] = new bdi(this, 32, 0);
        this.head[2] = new bdi(this, 0, 16);

        for (int i = 0; i < 3; i++)
        {
            this.head[i].a(-4.0F, -4.0F, -4.0F, 8, 8, 8, f);
            this.head[i].a(0.0F, 0.0F + f1, 0.0F);
        }
    }

    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        setRotationAngles(f, f1, f2, f3, f4, f5);
        GL11.glTranslatef(0.0F, 0.75F, 0.0F);
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glRotatef(this.sinage[0] * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
        this.head[0].a(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(this.sinage[1] * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
        this.head[1].a(f5);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(this.sinage[2] * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
        this.head[2].a(f5);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
        for (int i = 0; i < 3; i++)
        {
            this.head[i].g = (f3 / (180F / (float)Math.PI));
            this.head[i].f = (f4 / (180F / (float)Math.PI));
        }
    }
}

