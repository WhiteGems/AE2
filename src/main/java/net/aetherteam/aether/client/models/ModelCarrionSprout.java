package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelMinecart;
import net.minecraft.entity.Entity;
import net.minecraft.src.bdi;
import org.lwjgl.opengl.GL11;

public class ModelCarrionSprout extends ModelMinecart
{
    bdi Pedal4;
    bdi Pedal3;
    bdi Pedal2;
    bdi Pedal1;
    bdi TopStem;
    bdi BottomStem;
    bdi HeadRoof;
    bdi Teeth;
    bdi Jaw;
    bdi Head;
    private bdi[] petal;
    private static int petals = 8;
    public float sinage;
    public float sinage2;
    private float pie = ((float)Math.PI * 2F);

    public ModelCarrionSprout()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.petal = new bdi[petals];

        for (int i = 0; i < petals; i++)
        {
            this.petal[i] = new bdi(this, 43, 49);

            if (i % 2 == 0)
            {
                this.petal[i] = new bdi(this, 43, 49);
                this.petal[i].a(-2.8F, -1.0F, -10.8F, 6, 0, 9);
                this.petal[i].a(0.0F, 1.0F, 0.0F);
            }
            else
            {
                this.petal[i].a(-2.8F, -1.0F, -11.8F, 6, 0, 9);
                this.petal[i].a(0.0F, 1.0F, 0.0F);
            }
        }

        this.Head = new bdi(this, 0, 0);
        this.Head.a(-3.0F, -3.0F, -3.0F, 6, 2, 6);
        this.Head.a(0.0F, 0.0F, 0.0F);
        this.TopStem = new bdi(this, 8, 25);
        this.TopStem.a(0.0F, 0.0F, 0.0F, 2, 6, 2);
        this.TopStem.a(-1.0F, 14.0F, -3.0F);
        this.TopStem.b(64, 64);
        this.TopStem.i = true;
        setRotation(this.TopStem, 0.349066F, 0.0F, 0.0F);
        this.BottomStem = new bdi(this, 0, 25);
        this.BottomStem.a(0.0F, 0.0F, 0.0F, 2, 5, 2);
        this.BottomStem.a(-1.0F, 19.0F, -1.0F);
        this.BottomStem.b(64, 64);
        this.BottomStem.i = true;
        setRotation(this.BottomStem, 0.0F, 0.0F, 0.0F);
        this.HeadRoof = new bdi(this, 20, 16);
        this.HeadRoof.a(0.0F, 0.0F, 0.0F, 11, 5, 11);
        this.HeadRoof.a(-5.5F, 4.0F, -7.5F);
        this.HeadRoof.b(64, 64);
        this.HeadRoof.i = true;
        setRotation(this.HeadRoof, -0.349066F, 0.0F, 0.0F);
        this.Teeth = new bdi(this, 0, 33);
        this.Teeth.a(0.0F, 0.0F, 0.0F, 9, 1, 9);
        this.Teeth.a(-4.5F, 8.5F, -8.5F);
        this.Teeth.b(64, 64);
        this.Teeth.i = true;
        setRotation(this.Teeth, -0.349066F, 0.0F, 0.0F);
        this.Jaw = new bdi(this, 24, 1);
        this.Jaw.a(0.0F, 0.0F, -9.0F, 10, 2, 10);
        this.Jaw.a(-5.0F, 12.0F, 0.0F);
        this.Jaw.b(64, 64);
        this.Jaw.i = true;
        setRotation(this.Jaw, 0.0F, 0.0F, 0.0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        for (int i = 0; i < petals; i++)
        {
            this.petal[i].a(f5);
        }

        this.TopStem.a(f5);
        this.BottomStem.a(f5);
        this.HeadRoof.a(f5);
        this.Teeth.a(f5);
        this.Jaw.a(f5);
        this.Head.a(f5);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.3F, 0.0F);
        this.BottomStem.a(f5);
        GL11.glPopMatrix();
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
        float yOffset = 21.0F;

        for (int i = 0; i < petals; i++)
        {
            this.petal[i].f = (i % 2 == 0 ? -0.25F : -0.4125F);
            this.petal[i].f += this.sinage;
            this.petal[i].g = 17.0F;
            this.petal[i].g += this.pie / petals * i;
            this.petal[i].d = (boff + yOffset);
        }

        this.Jaw.f = 0.08F;
        this.Jaw.f += this.sinage;
        this.Head.d = (boff + yOffset + this.sinage * 2.0F);
        this.Jaw.d = (boff + 8.0F + this.sinage * 2.0F);
        this.BottomStem.d = (boff + 15.0F + this.sinage * 2.0F);
        this.TopStem.d = (boff + 10.0F + this.sinage * 2.0F);
        this.HeadRoof.d = (boff + this.sinage * 2.0F);
        this.Teeth.d = (boff + 4.5F + this.sinage * 2.0F);
    }
}

