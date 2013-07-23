package net.aetherteam.aether.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.src.bdi;

@SideOnly(Side.CLIENT)
public class ModelAltar1 extends ModelMinecart
{
    public bdi TopBase;
    public bdi CornerTopLeft;
    public bdi AmbrosiumTopLeft;
    public bdi CornerBottomLeft;
    public bdi CornerBottomRight;
    public bdi CornerTopRight;
    public bdi AmbrosiumTopRight;
    public bdi AmbrosiumBottomLeft;
    public bdi AmbrosiumBottomRight;
    public bdi LowerTopBase;
    public bdi SupportPole;
    public bdi BottomBase;
    public bdi UpperBottomBase;
    public bdi Scroll;
    private float offsetX = 0.0F;
    private float offsetY = 0.0F;
    private float offsetZ = 0.0F;

    public ModelAltar1()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.TopBase = new bdi(this, 8, 15).b(64, 64);
        this.TopBase.a(0.0F, 0.0F, 0.0F, 14, 3, 14);
        setRotationPointWithOffset(this.TopBase, -7.0F, 9.0F, -7.0F);
        this.TopBase.i = true;
        setRotation(this.TopBase, 0.0F, 0.0F, 0.0F);
        this.CornerTopLeft = new bdi(this, 48, 6).b(64, 64);
        this.CornerTopLeft.a(0.0F, 0.0F, 0.0F, 4, 5, 4);
        setRotationPointWithOffset(this.CornerTopLeft, 4.0F, 8.0F, 4.0F);
        this.CornerTopLeft.i = true;
        setRotation(this.CornerTopLeft, 0.0F, 0.0F, 0.0F);
        this.AmbrosiumTopLeft = new bdi(this, 0, 29).b(64, 64);
        this.AmbrosiumTopLeft.a(-1.0F, 0.0F, -1.0F, 2, 1, 2);
        setRotationPointWithOffset(this.AmbrosiumTopLeft, 6.0F, 7.0F, 6.0F);
        this.AmbrosiumTopLeft.i = true;
        setRotation(this.AmbrosiumTopLeft, 0.0F, 0.0F, 0.0F);
        this.CornerBottomLeft = new bdi(this, 48, 6).b(64, 64);
        this.CornerBottomLeft.a(0.0F, 0.0F, 0.0F, 4, 5, 4);
        setRotationPointWithOffset(this.CornerBottomLeft, -8.0F, 8.0F, 4.0F);
        this.CornerBottomLeft.i = true;
        setRotation(this.CornerBottomLeft, 0.0F, 0.0F, 0.0F);
        this.CornerBottomRight = new bdi(this, 48, 6).b(64, 64);
        this.CornerBottomRight.a(0.0F, 0.0F, 0.0F, 4, 5, 4);
        setRotationPointWithOffset(this.CornerBottomRight, -8.0F, 8.0F, -8.0F);
        this.CornerBottomRight.i = true;
        setRotation(this.CornerBottomRight, 0.0F, 0.0F, 0.0F);
        this.CornerTopRight = new bdi(this, 48, 6).b(64, 64);
        this.CornerTopRight.a(0.0F, 0.0F, 0.0F, 4, 5, 4);
        setRotationPointWithOffset(this.CornerTopRight, 4.0F, 8.0F, -8.0F);
        this.CornerTopRight.i = true;
        setRotation(this.CornerTopRight, 0.0F, 0.0F, 0.0F);
        this.AmbrosiumTopRight = new bdi(this, 0, 29).b(64, 64);
        this.AmbrosiumTopRight.a(-1.0F, 0.0F, -1.0F, 2, 1, 2);
        setRotationPointWithOffset(this.AmbrosiumTopRight, 6.0F, 7.0F, -6.0F);
        this.AmbrosiumTopRight.i = true;
        setRotation(this.AmbrosiumTopRight, 0.0F, 0.0F, 0.0F);
        this.AmbrosiumBottomLeft = new bdi(this, 0, 29).b(64, 64);
        this.AmbrosiumBottomLeft.a(-1.0F, 0.0F, -1.0F, 2, 1, 2);
        setRotationPointWithOffset(this.AmbrosiumBottomLeft, -6.0F, 7.0F, 6.0F);
        this.AmbrosiumBottomLeft.i = true;
        setRotation(this.AmbrosiumBottomLeft, 0.0F, 0.0F, 0.0F);
        this.AmbrosiumBottomRight = new bdi(this, 0, 29).b(64, 64);
        this.AmbrosiumBottomRight.a(-1.0F, 0.0F, -1.0F, 2, 1, 2);
        setRotationPointWithOffset(this.AmbrosiumBottomRight, -6.0F, 7.0F, -6.0F);
        this.AmbrosiumBottomRight.i = true;
        setRotation(this.AmbrosiumBottomRight, 0.0F, 0.0F, 0.0F);
        this.LowerTopBase = new bdi(this, 0, 0).b(64, 64);
        this.LowerTopBase.a(0.0F, 0.0F, 0.0F, 6, 3, 6);
        setRotationPointWithOffset(this.LowerTopBase, -3.0F, 12.0F, -3.0F);
        this.LowerTopBase.i = true;
        setRotation(this.LowerTopBase, 0.0F, 0.0F, 0.0F);
        this.SupportPole = new bdi(this, 0, 22).b(64, 64);
        this.SupportPole.a(0.0F, 0.0F, 0.0F, 2, 5, 2);
        setRotationPointWithOffset(this.SupportPole, -1.0F, 15.0F, -1.0F);
        this.SupportPole.i = true;
        setRotation(this.SupportPole, 0.0F, 0.0F, 0.0F);
        this.BottomBase = new bdi(this, 8, 32).b(64, 64);
        this.BottomBase.a(0.0F, 0.0F, 0.0F, 14, 2, 14);
        setRotationPointWithOffset(this.BottomBase, -7.0F, 22.0F, -7.0F);
        this.BottomBase.i = true;
        setRotation(this.BottomBase, 0.0F, 0.0F, 0.0F);
        this.UpperBottomBase = new bdi(this, 16, 5).b(64, 64);
        this.UpperBottomBase.a(0.0F, 0.0F, 0.0F, 8, 2, 8);
        setRotationPointWithOffset(this.UpperBottomBase, -4.0F, 20.0F, -4.0F);
        this.UpperBottomBase.i = true;
        setRotation(this.UpperBottomBase, 0.0F, 0.0F, 0.0F);
        this.Scroll = new bdi(this, 0, 56).b(64, 64);
        this.Scroll.a(0.0F, 0.0F, 0.0F, 8, 2, 6);
        setRotationPointWithOffset(this.Scroll, -4.0F, 8.4F, -3.0F);
        this.Scroll.i = true;
        setRotation(this.Scroll, 0.0F, 0.0F, -0.10472F);
    }

    public void renderAll(float f5)
    {
        this.TopBase.a(f5);
        this.CornerTopLeft.a(f5);
        this.AmbrosiumTopLeft.a(f5);
        this.CornerBottomLeft.a(f5);
        this.CornerBottomRight.a(f5);
        this.CornerTopRight.a(f5);
        this.AmbrosiumTopRight.a(f5);
        this.AmbrosiumBottomLeft.a(f5);
        this.AmbrosiumBottomRight.a(f5);
        this.LowerTopBase.a(f5);
        this.SupportPole.a(f5);
        this.BottomBase.a(f5);
        this.UpperBottomBase.a(f5);
        this.Scroll.a(f5);
    }

    private void setRotation(bdi model, float x, float y, float z)
    {
        model.f = x;
        model.g = y;
        model.h = z;
    }

    private void setRotationPointWithOffset(bdi renderer, float x, float y, float z)
    {
        renderer.a(x + this.offsetX, y + this.offsetY, z + this.offsetZ);
    }
}

