package net.aetherteam.aether.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelAltar2 extends ModelBase
{
    ModelRenderer UpperBottomBase;
    ModelRenderer Scroll;
    private float offsetX = 8.0F;
    private float offsetY = -8.0F;
    private float offsetZ = 8.0F;

    public ModelAltar2()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.UpperBottomBase = (new ModelRenderer(this, 16, 5)).setTextureSize(64, 64);
        this.UpperBottomBase.addBox(0.0F, 0.0F, 0.0F, 8, 2, 8);
        this.setRotationPointWithOffset(this.UpperBottomBase, -4.0F, 20.0F, -4.0F);
        this.UpperBottomBase.mirror = true;
        this.setRotation(this.UpperBottomBase, 0.0F, 0.0F, 0.0F);
        this.Scroll = (new ModelRenderer(this, 0, 56)).setTextureSize(64, 64);
        this.Scroll.addBox(0.0F, 0.0F, 0.0F, 8, 2, 6);
        this.setRotationPointWithOffset(this.Scroll, -4.0F, 8.4F, -3.0F);
        this.Scroll.mirror = true;
        this.setRotation(this.Scroll, 0.0F, 0.0F, -0.1047198F);
    }

    public void renderAll(float var1)
    {
        this.UpperBottomBase.render(var1);
        this.Scroll.render(var1);
    }

    private void setRotation(ModelRenderer var1, float var2, float var3, float var4)
    {
        var1.rotateAngleX = var2;
        var1.rotateAngleY = var3;
        var1.rotateAngleZ = var4;
    }

    private void setRotationPointWithOffset(ModelRenderer var1, float var2, float var3, float var4)
    {
        var1.setRotationPoint(var2 + this.offsetX, var3 + this.offsetY, var4 + this.offsetZ);
    }
}
