package net.aetherteam.aether.donator;

public class DonatorTexture
{
    public int imageWidth;
    public int imageHeight;
    public String onlineURL;
    public String localURL;

    public DonatorTexture(String var1, String var2, int var3, int var4)
    {
        this.localURL = var1;
        this.onlineURL = var2;
        this.imageWidth = var3;
        this.imageHeight = var4;
    }
}
