package net.aetherteam.aether.client.models;

import net.aetherteam.aether.entities.mounts.EntityPhyg;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelPhyg2 extends ModelBase
{
    private ModelRenderer leftWingInner = new ModelRenderer(this, 0, 0);
    private ModelRenderer leftWingOuter = new ModelRenderer(this, 20, 0);
    private ModelRenderer rightWingInner = new ModelRenderer(this, 0, 0);
    private ModelRenderer rightWingOuter = new ModelRenderer(this, 40, 0);
    public static EntityPhyg pig;

    public ModelPhyg2()
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
        float wingBend;
        float x;
        float y;
        float z;
        float x2;
        float y2;

        if (this.isChild)
        {
            wingBend = 2.0F;
            GL11.glPushMatrix();
            GL11.glPopMatrix();
            GL11.glScalef(1.0F / wingBend, 1.0F / wingBend, 1.0F / wingBend);
            GL11.glTranslatef(0.0F, 24.0F * f5, 0.0F);
            x = -((float)Math.acos((double)pig.wingFold));
            y = 32.0F * pig.wingFold / 4.0F;
            z = -32.0F * (float)Math.sqrt((double)(1.0F - pig.wingFold * pig.wingFold)) / 4.0F;
            x2 = 0.0F;
            y2 = y * (float)Math.cos((double)pig.wingAngle) - z * (float)Math.sin((double)pig.wingAngle);
            float y21 = y * (float)Math.sin((double)pig.wingAngle) + z * (float)Math.cos((double)pig.wingAngle);
            this.leftWingInner.setRotationPoint(4.0F + y2, y21 + 12.0F, x2);
            this.rightWingInner.setRotationPoint(-4.0F - y2, y21 + 12.0F, x2);
            y *= 3.0F;
            y2 = y * (float)Math.cos((double)pig.wingAngle) - z * (float)Math.sin((double)pig.wingAngle);
            y21 = y * (float)Math.sin((double)pig.wingAngle) + z * (float)Math.cos((double)pig.wingAngle);
            this.leftWingOuter.setRotationPoint(4.0F + y2, y21 + 12.0F, x2);
            this.rightWingOuter.setRotationPoint(-4.0F - y2, y21 + 12.0F, x2);
            this.leftWingInner.rotateAngleZ = pig.wingAngle + x + ((float)Math.PI / 2F);
            this.leftWingOuter.rotateAngleZ = pig.wingAngle - x + ((float)Math.PI / 2F);
            this.rightWingInner.rotateAngleZ = -(pig.wingAngle + x - ((float)Math.PI / 2F));
            this.rightWingOuter.rotateAngleZ = -(pig.wingAngle - x + ((float)Math.PI / 2F));
            this.leftWingOuter.render(f5);
            this.leftWingInner.render(f5);
            this.rightWingOuter.render(f5);
            this.rightWingInner.render(f5);
        }
        else
        {
            wingBend = -((float)Math.acos((double)pig.wingFold));
            x = 32.0F * pig.wingFold / 4.0F;
            y = -32.0F * (float)Math.sqrt((double)(1.0F - pig.wingFold * pig.wingFold)) / 4.0F;
            z = 0.0F;
            x2 = x * (float)Math.cos((double)pig.wingAngle) - y * (float)Math.sin((double)pig.wingAngle);
            y2 = x * (float)Math.sin((double)pig.wingAngle) + y * (float)Math.cos((double)pig.wingAngle);
            this.leftWingInner.setRotationPoint(4.0F + x2, y2 + 12.0F, z);
            this.rightWingInner.setRotationPoint(-4.0F - x2, y2 + 12.0F, z);
            x *= 3.0F;
            x2 = x * (float)Math.cos((double)pig.wingAngle) - y * (float)Math.sin((double)pig.wingAngle);
            y2 = x * (float)Math.sin((double)pig.wingAngle) + y * (float)Math.cos((double)pig.wingAngle);
            this.leftWingOuter.setRotationPoint(4.0F + x2, y2 + 12.0F, z);
            this.rightWingOuter.setRotationPoint(-4.0F - x2, y2 + 12.0F, z);
            this.leftWingInner.rotateAngleZ = pig.wingAngle + wingBend + ((float)Math.PI / 2F);
            this.leftWingOuter.rotateAngleZ = pig.wingAngle - wingBend + ((float)Math.PI / 2F);
            this.rightWingInner.rotateAngleZ = -(pig.wingAngle + wingBend - ((float)Math.PI / 2F));
            this.rightWingOuter.rotateAngleZ = -(pig.wingAngle - wingBend + ((float)Math.PI / 2F));
            this.leftWingOuter.render(f5);
            this.leftWingInner.render(f5);
            this.rightWingOuter.render(f5);
            this.rightWingInner.render(f5);
        }
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5) {}
}
