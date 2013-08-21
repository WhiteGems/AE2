package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.EntitySheepuff;
import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class ModelSheepuff2 extends ModelQuadruped
{
    private float field_78152_i;

    public ModelSheepuff2()
    {
        super(12, 0.0F);
        this.head = new ModelRenderer(this, 0, 0);
        this.head.addBox(-3.0F, -4.0F, -6.0F, 6, 6, 8, 0.0F);
        this.head.setRotationPoint(0.0F, 6.0F, -8.0F);
        this.body = new ModelRenderer(this, 28, 8);
        this.body.addBox(-4.0F, -10.0F, -7.0F, 8, 16, 6, 0.0F);
        this.body.setRotationPoint(0.0F, 5.0F, 2.0F);
    }

    public void setLivingAnimations(EntityLiving par1EntityLiving, float par2, float par3, float par4)
    {
        super.setLivingAnimations(par1EntityLiving, par2, par3, par4);
        this.head.rotationPointY = 6.0F + ((EntitySheepuff)par1EntityLiving).func_70894_j(par4) * 9.0F;
        this.field_78152_i = ((EntitySheepuff)par1EntityLiving).func_70890_k(par4);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity)
    {
        super.setRotationAngles(par1, par2, par3, par4, par5, par6, par7Entity);
        this.head.rotateAngleX = this.field_78152_i;
    }
}
