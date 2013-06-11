package net.aetherteam.aether.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

@SideOnly(Side.CLIENT)
public class ModelAltar1 extends ModelBase
{
    public ModelRenderer TopBase;
    public ModelRenderer CornerTopLeft;
    public ModelRenderer AmbrosiumTopLeft;
    public ModelRenderer CornerBottomLeft;
    public ModelRenderer CornerBottomRight;
    public ModelRenderer CornerTopRight;
    public ModelRenderer AmbrosiumTopRight;
    public ModelRenderer AmbrosiumBottomLeft;
    public ModelRenderer AmbrosiumBottomRight;
    public ModelRenderer LowerTopBase;
    public ModelRenderer SupportPole;
    public ModelRenderer BottomBase;
    public ModelRenderer UpperBottomBase;
    public ModelRenderer Scroll;
    private float offsetX = 0.0F;
    private float offsetY = 0.0F;
    private float offsetZ = 0.0F;

    public ModelAltar1()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.TopBase = (new ModelRenderer(this, 8, 15)).setTextureSize(64, 64);
        this.TopBase.addBox(0.0F, 0.0F, 0.0F, 14, 3, 14);
        this.setRotationPointWithOffset(this.TopBase, -7.0F, 9.0F, -7.0F);
        this.TopBase.mirror = true;
        this.setRotation(this.TopBase, 0.0F, 0.0F, 0.0F);
        this.CornerTopLeft = (new ModelRenderer(this, 48, 6)).setTextureSize(64, 64);
        this.CornerTopLeft.addBox(0.0F, 0.0F, 0.0F, 4, 5, 4);
        this.setRotationPointWithOffset(this.CornerTopLeft, 4.0F, 8.0F, 4.0F);
        this.CornerTopLeft.mirror = true;
        this.setRotation(this.CornerTopLeft, 0.0F, 0.0F, 0.0F);
        this.AmbrosiumTopLeft = (new ModelRenderer(this, 0, 29)).setTextureSize(64, 64);
        this.AmbrosiumTopLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2);
        this.setRotationPointWithOffset(this.AmbrosiumTopLeft, 6.0F, 7.0F, 6.0F);
        this.AmbrosiumTopLeft.mirror = true;
        this.setRotation(this.AmbrosiumTopLeft, 0.0F, 0.0F, 0.0F);
        this.CornerBottomLeft = (new ModelRenderer(this, 48, 6)).setTextureSize(64, 64);
        this.CornerBottomLeft.addBox(0.0F, 0.0F, 0.0F, 4, 5, 4);
        this.setRotationPointWithOffset(this.CornerBottomLeft, -8.0F, 8.0F, 4.0F);
        this.CornerBottomLeft.mirror = true;
        this.setRotation(this.CornerBottomLeft, 0.0F, 0.0F, 0.0F);
        this.CornerBottomRight = (new ModelRenderer(this, 48, 6)).setTextureSize(64, 64);
        this.CornerBottomRight.addBox(0.0F, 0.0F, 0.0F, 4, 5, 4);
        this.setRotationPointWithOffset(this.CornerBottomRight, -8.0F, 8.0F, -8.0F);
        this.CornerBottomRight.mirror = true;
        this.setRotation(this.CornerBottomRight, 0.0F, 0.0F, 0.0F);
        this.CornerTopRight = (new ModelRenderer(this, 48, 6)).setTextureSize(64, 64);
        this.CornerTopRight.addBox(0.0F, 0.0F, 0.0F, 4, 5, 4);
        this.setRotationPointWithOffset(this.CornerTopRight, 4.0F, 8.0F, -8.0F);
        this.CornerTopRight.mirror = true;
        this.setRotation(this.CornerTopRight, 0.0F, 0.0F, 0.0F);
        this.AmbrosiumTopRight = (new ModelRenderer(this, 0, 29)).setTextureSize(64, 64);
        this.AmbrosiumTopRight.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2);
        this.setRotationPointWithOffset(this.AmbrosiumTopRight, 6.0F, 7.0F, -6.0F);
        this.AmbrosiumTopRight.mirror = true;
        this.setRotation(this.AmbrosiumTopRight, 0.0F, 0.0F, 0.0F);
        this.AmbrosiumBottomLeft = (new ModelRenderer(this, 0, 29)).setTextureSize(64, 64);
        this.AmbrosiumBottomLeft.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2);
        this.setRotationPointWithOffset(this.AmbrosiumBottomLeft, -6.0F, 7.0F, 6.0F);
        this.AmbrosiumBottomLeft.mirror = true;
        this.setRotation(this.AmbrosiumBottomLeft, 0.0F, 0.0F, 0.0F);
        this.AmbrosiumBottomRight = (new ModelRenderer(this, 0, 29)).setTextureSize(64, 64);
        this.AmbrosiumBottomRight.addBox(-1.0F, 0.0F, -1.0F, 2, 1, 2);
        this.setRotationPointWithOffset(this.AmbrosiumBottomRight, -6.0F, 7.0F, -6.0F);
        this.AmbrosiumBottomRight.mirror = true;
        this.setRotation(this.AmbrosiumBottomRight, 0.0F, 0.0F, 0.0F);
        this.LowerTopBase = (new ModelRenderer(this, 0, 0)).setTextureSize(64, 64);
        this.LowerTopBase.addBox(0.0F, 0.0F, 0.0F, 6, 3, 6);
        this.setRotationPointWithOffset(this.LowerTopBase, -3.0F, 12.0F, -3.0F);
        this.LowerTopBase.mirror = true;
        this.setRotation(this.LowerTopBase, 0.0F, 0.0F, 0.0F);
        this.SupportPole = (new ModelRenderer(this, 0, 22)).setTextureSize(64, 64);
        this.SupportPole.addBox(0.0F, 0.0F, 0.0F, 2, 5, 2);
        this.setRotationPointWithOffset(this.SupportPole, -1.0F, 15.0F, -1.0F);
        this.SupportPole.mirror = true;
        this.setRotation(this.SupportPole, 0.0F, 0.0F, 0.0F);
        this.BottomBase = (new ModelRenderer(this, 8, 32)).setTextureSize(64, 64);
        this.BottomBase.addBox(0.0F, 0.0F, 0.0F, 14, 2, 14);
        this.setRotationPointWithOffset(this.BottomBase, -7.0F, 22.0F, -7.0F);
        this.BottomBase.mirror = true;
        this.setRotation(this.BottomBase, 0.0F, 0.0F, 0.0F);
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
        this.TopBase.render(var1);
        this.CornerTopLeft.render(var1);
        this.AmbrosiumTopLeft.render(var1);
        this.CornerBottomLeft.render(var1);
        this.CornerBottomRight.render(var1);
        this.CornerTopRight.render(var1);
        this.AmbrosiumTopRight.render(var1);
        this.AmbrosiumBottomLeft.render(var1);
        this.AmbrosiumBottomRight.render(var1);
        this.LowerTopBase.render(var1);
        this.SupportPole.render(var1);
        this.BottomBase.render(var1);
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
