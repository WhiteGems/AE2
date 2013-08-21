package net.aetherteam.aether.donator;

import net.minecraft.util.ResourceLocation;

public class DonatorTexture
{
    public int imageWidth;
    public int imageHeight;
    public ResourceLocation onlineURL;
    public ResourceLocation localURL;

    public DonatorTexture(String local, String url, int width, int height)
    {
        this.localURL = new ResourceLocation(local);
        this.onlineURL = new ResourceLocation(url);
        this.imageWidth = width;
        this.imageHeight = height;
    }
}
