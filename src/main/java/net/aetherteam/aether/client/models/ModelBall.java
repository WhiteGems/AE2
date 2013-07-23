package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelBall extends ModelBase
{
    public ModelRenderer[] head;
    public float[] sinage;
    private static final float sponge = (180F / (float)Math.PI);

    public ModelBall()
    {
        this(0.0F);
    }

    public ModelBall(float var1)
    {
        this(var1, 0.0F);
    }

    public ModelBall(float var1, float var2)
    {
        this.sinage = new float[3];
        this.head = new ModelRenderer[3];
        this.head[0] = new ModelRenderer(this, 0, 0);
        this.head[1] = new ModelRenderer(this, 32, 0);
        this.head[2] = new ModelRenderer(this, 0, 16);

        for (int var3 = 0; var3 < 3; ++var3)
        {
            this.head[var3].addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, var1);
            this.head[var3].setRotationPoint(0.0F, 0.0F + var2, 0.0F);
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        this.setRotationAngles(var2, var3, var4, var5, var6, var7);
        GL11.glTranslatef(0.0F, 0.75F, 0.0F);
        GL11.glEnable(GL11.GL_NORMALIZE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPushMatrix();
        GL11.glRotatef(this.sinage[0] * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
        this.head[0].render(var7);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(this.sinage[1] * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
        this.head[1].render(var7);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(this.sinage[2] * (180F / (float)Math.PI), 0.0F, 0.0F, 1.0F);
        this.head[2].render(var7);
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6)
    {
        for (int var7 = 0; var7 < 3; ++var7)
        {
            this.head[var7].rotateAngleY = var4 / (180F / (float)Math.PI);
            this.head[var7].rotateAngleX = var5 / (180F / (float)Math.PI);
        }
    }
}
