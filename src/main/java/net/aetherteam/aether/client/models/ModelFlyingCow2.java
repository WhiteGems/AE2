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
        this.rightWingOuter.rotateAngleY = (float)Math.PI;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity e, float f, float f1, float f2, float f3, float f4, float f5)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -10.0F * f5, 0.0F);
        float wingBend = -((float)Math.acos((double)flyingcow.wingFold));
        float x = 32.0F * flyingcow.wingFold / 4.0F;
        float y = -32.0F * (float)Math.sqrt((double)(1.0F - flyingcow.wingFold * flyingcow.wingFold)) / 4.0F;
        float z = 0.0F;
        float x2 = x * (float)Math.cos((double)flyingcow.wingAngle) - y * (float)Math.sin((double)flyingcow.wingAngle);
        float y2 = x * (float)Math.sin((double)flyingcow.wingAngle) + y * (float)Math.cos((double)flyingcow.wingAngle);
        this.leftWingInner.setRotationPoint(4.0F + x2, y2 + 12.0F, z);
        this.rightWingInner.setRotationPoint(-4.0F - x2, y2 + 12.0F, z);
        x *= 3.0F;
        x2 = x * (float)Math.cos((double)flyingcow.wingAngle) - y * (float)Math.sin((double)flyingcow.wingAngle);
        y2 = x * (float)Math.sin((double)flyingcow.wingAngle) + y * (float)Math.cos((double)flyingcow.wingAngle);
        this.leftWingOuter.setRotationPoint(4.0F + x2, y2 + 12.0F, z);
        this.rightWingOuter.setRotationPoint(-4.0F - x2, y2 + 12.0F, z);
        this.leftWingInner.rotateAngleZ = flyingcow.wingAngle + wingBend + ((float)Math.PI / 2F);
        this.leftWingOuter.rotateAngleZ = flyingcow.wingAngle - wingBend + ((float)Math.PI / 2F);
        this.rightWingInner.rotateAngleZ = -(flyingcow.wingAngle + wingBend - ((float)Math.PI / 2F));
        this.rightWingOuter.rotateAngleZ = -(flyingcow.wingAngle - wingBend + ((float)Math.PI / 2F));
        this.leftWingOuter.render(f5);
        this.leftWingInner.render(f5);
        this.rightWingOuter.render(f5);
        this.rightWingInner.render(f5);
        GL11.glPopMatrix();
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {}
}
