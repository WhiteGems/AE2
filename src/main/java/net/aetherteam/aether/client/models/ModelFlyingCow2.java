package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.mounts.EntityFlyingCow;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelFlyingCow2 extends ModelBase
{
    private ModelRenderer leftWingInner = new ModelRenderer(this, 0, 0);
    private ModelRenderer leftWingOuter = new ModelRenderer(this, 20, 0);
    private ModelRenderer rightWingInner = new ModelRenderer(this, 0, 0);
    private ModelRenderer rightWingOuter = new ModelRenderer(this, 40, 0);
    public static EntityFlyingCow flyingcow;

    public ModelFlyingCow2()
    {
        this.leftWingInner.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.leftWingOuter.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingInner.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingOuter.addBox(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingOuter.rotateAngleY = (float) Math.PI;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity var1, float var2, float var3, float var4, float var5, float var6, float var7)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -10.0F * var7, 0.0F);
        float var8 = -((float) Math.acos((double) flyingcow.wingFold));
        float var9 = 32.0F * flyingcow.wingFold / 4.0F;
        float var10 = -32.0F * (float) Math.sqrt((double) (1.0F - flyingcow.wingFold * flyingcow.wingFold)) / 4.0F;
        float var11 = 0.0F;
        float var12 = var9 * (float) Math.cos((double) flyingcow.wingAngle) - var10 * (float) Math.sin((double) flyingcow.wingAngle);
        float var13 = var9 * (float) Math.sin((double) flyingcow.wingAngle) + var10 * (float) Math.cos((double) flyingcow.wingAngle);
        this.leftWingInner.setRotationPoint(4.0F + var12, var13 + 12.0F, var11);
        this.rightWingInner.setRotationPoint(-4.0F - var12, var13 + 12.0F, var11);
        var9 *= 3.0F;
        var12 = var9 * (float) Math.cos((double) flyingcow.wingAngle) - var10 * (float) Math.sin((double) flyingcow.wingAngle);
        var13 = var9 * (float) Math.sin((double) flyingcow.wingAngle) + var10 * (float) Math.cos((double) flyingcow.wingAngle);
        this.leftWingOuter.setRotationPoint(4.0F + var12, var13 + 12.0F, var11);
        this.rightWingOuter.setRotationPoint(-4.0F - var12, var13 + 12.0F, var11);
        this.leftWingInner.rotateAngleZ = flyingcow.wingAngle + var8 + ((float) Math.PI / 2F);
        this.leftWingOuter.rotateAngleZ = flyingcow.wingAngle - var8 + ((float) Math.PI / 2F);
        this.rightWingInner.rotateAngleZ = -(flyingcow.wingAngle + var8 - ((float) Math.PI / 2F));
        this.rightWingOuter.rotateAngleZ = -(flyingcow.wingAngle - var8 + ((float) Math.PI / 2F));
        this.leftWingOuter.render(var7);
        this.leftWingInner.render(var7);
        this.rightWingOuter.render(var7);
        this.rightWingInner.render(var7);
        GL11.glPopMatrix();
    }

    public void setRotationAngles(float var1, float var2, float var3, float var4, float var5, float var6) {}
}
