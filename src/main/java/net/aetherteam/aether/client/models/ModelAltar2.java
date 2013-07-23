package net.aetherteam.aether.client.models;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelMinecart;
import net.minecraft.src.bdi;

@SideOnly(Side.CLIENT)
public class ModelAltar2 extends ModelMinecart
{
    bdi UpperBottomBase;
    bdi Scroll;
    private float offsetX = 8.0F;
    private float offsetY = -8.0F;
    private float offsetZ = 8.0F;

    public ModelAltar2()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
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

