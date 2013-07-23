package net.aetherteam.aether.donator;

public class DonatorTexture
{
    public int imageWidth;
    public int imageHeight;
    public String onlineURL;
    public String localURL;

    public DonatorTexture(String local, String url, int width, int height)
    {
        this.localURL = local;
        this.onlineURL = url;
        this.imageWidth = width;
        this.imageHeight = height;
    }
}

