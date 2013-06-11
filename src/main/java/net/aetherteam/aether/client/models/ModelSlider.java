package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelSlider extends ModelBase
{
    public ModelRenderer head;

    public ModelSlider()
    {
        this(0.0F);
    }

    public ModelSlider(float var1)
    {
        this(var1, 0.0F);
    }

    public ModelSlider(float var1, float var2)
    {
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-8.0F, -16.0F, -8.0F, 16, 16, 16, var1);
        this.head.setRotationPoint(0.0F, 0.0F + var2, 0.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        this.setRotationAngles(var2, var3, var4, var5, var6, var7);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        this.head.render(var7);
        GL11.glPopMatrix();
    }

    public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6)
    {
        this.head.rotateAngleY = 0.0F;
        this.head.rotateAngleX = 0.0F;
    }
}
