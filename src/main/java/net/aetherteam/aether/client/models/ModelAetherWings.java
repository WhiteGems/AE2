package net.aetherteam.aether.client.models;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelAetherWings extends ModelBiped
{
    public ModelRenderer wingLeft;
    public ModelRenderer wingRight;
    public float sinage;
    public boolean gonRound;

    public ModelAetherWings()
    {
        this(0.0F);
    }

    public ModelAetherWings(float f)
    {
        this(f, 0.0F);
    }

    public ModelAetherWings(float f, float f1)
    {
        this.heldItemLeft = 0;
        this.heldItemRight = 0;
        this.isSneak = false;
        this.wingLeft = new ModelRenderer(this, 24, 31);
        this.wingLeft.addBox(0.0F, -7.0F, 1.0F, 19, 8, 0, f);
        this.wingLeft.setRotationPoint(0.5F, 5.0F + f1, 2.625F);
        this.wingRight = new ModelRenderer(this, 24, 31);
        this.wingRight.mirror = true;
        this.wingRight.addBox(-19.0F, -7.0F, 1.0F, 19, 8, 0, f);
        this.wingRight.setRotationPoint(-0.5F, 5.0F + f1, 2.625F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, e);
        this.wingLeft.renderWithRotation(f5);
        this.wingRight.renderWithRotation(f5);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        this.wingLeft.rotateAngleY = -0.2F;
        this.wingRight.rotateAngleY = 0.2F;
        this.wingLeft.rotateAngleZ = -0.125F;
        this.wingRight.rotateAngleZ = 0.125F;
        this.wingLeft.rotateAngleY = (float)((double)this.wingLeft.rotateAngleY + Math.sin((double)this.sinage) / 6.0D);
        this.wingRight.rotateAngleY = (float)((double)this.wingRight.rotateAngleY - Math.sin((double)this.sinage) / 6.0D);
        this.wingLeft.rotateAngleZ = (float)((double)this.wingLeft.rotateAngleZ + Math.cos((double)this.sinage) / (double)(this.gonRound ? 8.0F : 3.0F));
        this.wingRight.rotateAngleZ = (float)((double)this.wingRight.rotateAngleZ - Math.cos((double)this.sinage) / (double)(this.gonRound ? 8.0F : 3.0F));
    }
}
