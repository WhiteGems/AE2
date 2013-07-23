package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelCarrionSprout extends ModelBase
{
    ModelRenderer Pedal4;
    ModelRenderer Pedal3;
    ModelRenderer Pedal2;
    ModelRenderer Pedal1;
    ModelRenderer TopStem;
    ModelRenderer BottomStem;
    ModelRenderer HeadRoof;
    ModelRenderer Teeth;
    ModelRenderer Jaw;
    ModelRenderer Head;
    private ModelRenderer[] petal;
    private static int petals = 8;
    public float sinage;
    public float sinage2;
    private float pie = ((float)Math.PI * 2F);

    public ModelCarrionSprout()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.petal = new ModelRenderer[petals];

        for (int var1 = 0; var1 < petals; ++var1)
        {
            this.petal[var1] = new ModelRenderer(this, 43, 49);

            if (var1 % 2 == 0)
            {
                this.petal[var1] = new ModelRenderer(this, 43, 49);
                this.petal[var1].addBox(-2.8F, -1.0F, -10.8F, 6, 0, 9);
                this.petal[var1].setRotationPoint(0.0F, 1.0F, 0.0F);
            }
            else
            {
                this.petal[var1].addBox(-2.8F, -1.0F, -11.8F, 6, 0, 9);
                this.petal[var1].setRotationPoint(0.0F, 1.0F, 0.0F);
            }
        }

        this.Head = new ModelRenderer(this, 0, 0);
        this.Head.addBox(-3.0F, -3.0F, -3.0F, 6, 2, 6);
        this.Head.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.TopStem = new ModelRenderer(this, 8, 25);
        this.TopStem.addBox(0.0F, 0.0F, 0.0F, 2, 6, 2);
        this.TopStem.setRotationPoint(-1.0F, 14.0F, -3.0F);
        this.TopStem.setTextureSize(64, 64);
        this.TopStem.mirror = true;
        this.setRotation(this.TopStem, 0.3490659F, 0.0F, 0.0F);
        this.BottomStem = new ModelRenderer(this, 0, 25);
        this.BottomStem.addBox(0.0F, 0.0F, 0.0F, 2, 5, 2);
        this.BottomStem.setRotationPoint(-1.0F, 19.0F, -1.0F);
        this.BottomStem.setTextureSize(64, 64);
        this.BottomStem.mirror = true;
        this.setRotation(this.BottomStem, 0.0F, 0.0F, 0.0F);
        this.HeadRoof = new ModelRenderer(this, 20, 16);
        this.HeadRoof.addBox(0.0F, 0.0F, 0.0F, 11, 5, 11);
        this.HeadRoof.setRotationPoint(-5.5F, 4.0F, -7.5F);
        this.HeadRoof.setTextureSize(64, 64);
        this.HeadRoof.mirror = true;
        this.setRotation(this.HeadRoof, -0.3490659F, 0.0F, 0.0F);
        this.Teeth = new ModelRenderer(this, 0, 33);
        this.Teeth.addBox(0.0F, 0.0F, 0.0F, 9, 1, 9);
        this.Teeth.setRotationPoint(-4.5F, 8.5F, -8.5F);
        this.Teeth.setTextureSize(64, 64);
        this.Teeth.mirror = true;
        this.setRotation(this.Teeth, -0.3490659F, 0.0F, 0.0F);
        this.Jaw = new ModelRenderer(this, 24, 1);
        this.Jaw.addBox(0.0F, 0.0F, -9.0F, 10, 2, 10);
        this.Jaw.setRotationPoint(-5.0F, 12.0F, 0.0F);
        this.Jaw.setTextureSize(64, 64);
        this.Jaw.mirror = true;
        this.setRotation(this.Jaw, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        super.render(var1, var2, var3, var4, var5, var6, var7);
        this.setRotationAngles(var2, var3, var4, var5, var6, var7, var1);

        for (int var8 = 0; var8 < petals; ++var8)
        {
            this.petal[var8].render(var7);
        }

        this.TopStem.render(var7);
        this.BottomStem.render(var7);
        this.HeadRoof.render(var7);
        this.Teeth.render(var7);
        this.Jaw.render(var7);
        this.Head.render(var7);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, 0.3F, 0.0F);
        this.BottomStem.render(var7);
        GL11.glPopMatrix();
    }

    private void setRotation(ModelRenderer var1, float var2, float var3, float var4)
    {
        var1.rotateAngleX = var2;
        var1.rotateAngleY = var3;
        var1.rotateAngleZ = var4;
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6, Entity var7)
    {
        super.setRotationAngles(var1, var2, var3, var4, var5, var6, var7);
        float var8 = this.sinage2;
        float var9 = 21.0F;

        for (int var10 = 0; var10 < petals; ++var10)
        {
            this.petal[var10].rotateAngleX = var10 % 2 == 0 ? -0.25F : -0.4125F;
            this.petal[var10].rotateAngleX += this.sinage;
            this.petal[var10].rotateAngleY = 17.0F;
            this.petal[var10].rotateAngleY += this.pie / (float)petals * (float)var10;
            this.petal[var10].rotationPointY = var8 + var9;
        }

        this.Jaw.rotateAngleX = 0.08F;
        this.Jaw.rotateAngleX += this.sinage;
        this.Head.rotationPointY = var8 + var9 + this.sinage * 2.0F;
        this.Jaw.rotationPointY = var8 + 8.0F + this.sinage * 2.0F;
        this.BottomStem.rotationPointY = var8 + 15.0F + this.sinage * 2.0F;
        this.TopStem.rotationPointY = var8 + 10.0F + this.sinage * 2.0F;
        this.HeadRoof.rotationPointY = var8 + this.sinage * 2.0F;
        this.Teeth.rotationPointY = var8 + 4.5F + this.sinage * 2.0F;
    }
}
