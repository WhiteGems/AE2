package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.mounts.EntityPhyg;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import org.lwjgl.opengl.GL11;

public class ModelPhyg2 extends ModelMinecart
{
    private bdi leftWingInner;
    private bdi leftWingOuter;
    private bdi rightWingInner;
    private bdi rightWingOuter;
    public static EntityPhyg pig;

    public ModelPhyg2()
    {
        this.leftWingInner = new bdi(this, 0, 0);
        this.leftWingOuter = new bdi(this, 20, 0);
        this.rightWingInner = new bdi(this, 0, 0);
        this.rightWingOuter = new bdi(this, 40, 0);
        this.leftWingInner.a(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.leftWingOuter.a(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingInner.a(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingOuter.a(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingOuter.g = (float)Math.PI;
    }

    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        if (this.isChild)
        {
            float f6 = 2.0F;
            GL11.glPushMatrix();
            GL11.glPopMatrix();
            GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
            GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
            float wingBend = -(float)Math.acos(pig.wingFold);
            float x = 32.0F * pig.wingFold / 4.0F;
            float y = -32.0F * (float)Math.sqrt(1.0F - pig.wingFold * pig.wingFold) / 4.0F;
            float z = 0.0F;
            float x2 = x * (float)Math.cos(pig.wingAngle) - y * (float)Math.sin(pig.wingAngle);
            float y2 = x * (float)Math.sin(pig.wingAngle) + y * (float)Math.cos(pig.wingAngle);
            this.leftWingInner.a(4.0F + x2, y2 + 12.0F, z);
            this.rightWingInner.a(-4.0F - x2, y2 + 12.0F, z);
            x *= 3.0F;
            x2 = x * (float)Math.cos(pig.wingAngle) - y * (float)Math.sin(pig.wingAngle);
            y2 = x * (float)Math.sin(pig.wingAngle) + y * (float)Math.cos(pig.wingAngle);
            this.leftWingOuter.a(4.0F + x2, y2 + 12.0F, z);
            this.rightWingOuter.a(-4.0F - x2, y2 + 12.0F, z);
            this.leftWingInner.h = (pig.wingAngle + wingBend + ((float)Math.PI / 2F));
            this.leftWingOuter.h = (pig.wingAngle - wingBend + ((float)Math.PI / 2F));
            this.rightWingInner.h = (-(pig.wingAngle + wingBend - ((float)Math.PI / 2F)));
            this.rightWingOuter.h = (-(pig.wingAngle - wingBend + ((float)Math.PI / 2F)));
            this.leftWingOuter.a(f5);
            this.leftWingInner.a(f5);
            this.rightWingOuter.a(f5);
            this.rightWingInner.a(f5);
        }
        else
        {
            float wingBend = -(float)Math.acos(pig.wingFold);
            float x = 32.0F * pig.wingFold / 4.0F;
            float y = -32.0F * (float)Math.sqrt(1.0F - pig.wingFold * pig.wingFold) / 4.0F;
            float z = 0.0F;
            float x2 = x * (float)Math.cos(pig.wingAngle) - y * (float)Math.sin(pig.wingAngle);
            float y2 = x * (float)Math.sin(pig.wingAngle) + y * (float)Math.cos(pig.wingAngle);
            this.leftWingInner.a(4.0F + x2, y2 + 12.0F, z);
            this.rightWingInner.a(-4.0F - x2, y2 + 12.0F, z);
            x *= 3.0F;
            x2 = x * (float)Math.cos(pig.wingAngle) - y * (float)Math.sin(pig.wingAngle);
            y2 = x * (float)Math.sin(pig.wingAngle) + y * (float)Math.cos(pig.wingAngle);
            this.leftWingOuter.a(4.0F + x2, y2 + 12.0F, z);
            this.rightWingOuter.a(-4.0F - x2, y2 + 12.0F, z);
            this.leftWingInner.h = (pig.wingAngle + wingBend + ((float)Math.PI / 2F));
            this.leftWingOuter.h = (pig.wingAngle - wingBend + ((float)Math.PI / 2F));
            this.rightWingInner.h = (-(pig.wingAngle + wingBend - ((float)Math.PI / 2F)));
            this.rightWingOuter.h = (-(pig.wingAngle - wingBend + ((float)Math.PI / 2F)));
            this.leftWingOuter.a(f5);
            this.leftWingInner.a(f5);
            this.rightWingOuter.a(f5);
            this.rightWingInner.a(f5);
        }
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {
    }
}

