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

/* Location:           D:\Dev\Mc\forge_orl\mcp\jars\bin\aether.jar
 * Qualified Name:     net.aetherteam.aether.donator.DonatorTexture
 * JD-Core Version:    0.6.2
 */